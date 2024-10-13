package log

import androidx.compose.runtime.mutableStateListOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import notifications.InfoManagerData
import utils.Colors.darkRed
import utils.EMPTY_STRING
import utils.LOG_MANAGER_NUMBER_OF_LINES
import utils.exportToFile
import utils.getStringResource
import utils.getTimeStamp
import utils.logLevelRegex
import utils.runCommand
import java.util.UUID

class LogManager(
    private val adbPath: String
) : LogManagerInterface {

    private var monitorJob: Job? = null
    private val _logs = mutableStateListOf<LogData>()
    private var currentProcess: Process? = null

    val logs: List<LogData>
        get() = _logs

    val isActive: Boolean
        get() = monitorJob?.isActive == true

    suspend fun exportLogs() {
        withContext(Dispatchers.IO) {
            buildString {
                logs.takeLast(LOG_MANAGER_NUMBER_OF_LINES).forEach { log ->
                    appendLine("${log.level} - ${log.time}")
                    appendLine(log.log)
                }
            }.exportToFile()
        }
    }

    fun setIsSelected(uuid: UUID) {
        runCatching {
            val logData = _logs.find { it.uuid == uuid } ?: throw NoSuchElementException("Log entry not found")
            val index = _logs.indexOf(logData)
            _logs[index] = logData.copy(isSelected = !logData.isSelected)
        }.onFailure { exception ->
            println("Failed to update log selection for UUID $uuid: ${exception.message}")
        }
    }

    override suspend fun startMonitoringLogs(
        coroutineScope: CoroutineScope,
        packageName: String,
        identifier: String,
        onMessage: (InfoManagerData) -> Unit
    ) {
        stopMonitoringLogs()
        _logs.clear()
        monitorJob = coroutineScope.launch {
            try {
                monitorLogs(
                    packageName = packageName.takeUnless { it == getStringResource("info.log.starting.package") },
                    identifier = identifier,
                    onMessage = onMessage
                )
            } catch (e: Exception) {
                LogData(
                    time = getTimeStamp(LOGGER_TIMESTAMP),
                    log = getStringResource("info.monitoring.stopped"),
                    level = LogLevel.NONE
                )
            }
        }
    }

    override suspend fun stopMonitoringLogs() {
        monitorJob?.cancel()
        currentProcess?.destroy()
    }

    override suspend fun clearLogs() {
        _logs.clear()
    }

    private suspend fun monitorLogs(
        packageName: String?,
        identifier: String,
        onMessage: (InfoManagerData) -> Unit
    ) {
        withContext(Dispatchers.IO) {
            var pid = EMPTY_STRING
            if (!packageName.isNullOrEmpty()) {
                pid = getPid(
                    identifier = identifier,
                    packageName = packageName
                )

                if (pid.isEmpty()) {
                    while (pid.isEmpty()) {
                        delay(1000)
                        pid = getPid(
                            identifier = identifier,
                            packageName = packageName
                        )
                    }
                    _logs.clear()
                }
            }
            val logcatProcess = ProcessBuilder(adbPath, "-s", identifier, "logcat").apply {
                if (pid.isNotEmpty()) {
                    command().add("--pid=$pid")
                }
            }.start().also {
                currentProcess = it
            }

            runCatching {
                logcatProcess.inputStream.bufferedReader().useLines { lines ->
                    lines.forEach { line ->
                        if (line.isNotEmpty()) {
                            val time = line.split(" ")[1].trim()
                            val (logType, text) = extractLogInfo(line) ?: (LogLevel.NONE to "")
                            _logs.add(LogData(time = time, log = text, level = logType))
                        }
                    }
                }
            }.onFailure { exception ->
                onMessage(
                    InfoManagerData(
                        message = "${getStringResource("info.log.error")} $identifier: $exception",
                        color = darkRed
                    )
                )
            }
        }
    }

    private fun getPid(
        identifier: String,
        packageName: String
    ) =
        "$adbPath -s $identifier shell pidof -s $packageName".runCommand()?.trim() ?: EMPTY_STRING

    private fun extractLogInfo(log: String): Pair<LogLevel, String>? {
        val matchResult = logLevelRegex.find(log)

        return matchResult?.let { result ->
            val logLevel = when (result.value[0]) {
                LogLevel.INFO.simplified() -> LogLevel.INFO
                LogLevel.ERROR.simplified() -> LogLevel.ERROR
                LogLevel.WARN.simplified() -> LogLevel.WARN
                LogLevel.VERBOSE.simplified() -> LogLevel.VERBOSE
                LogLevel.DEBUG.simplified() -> LogLevel.DEBUG
                else -> LogLevel.NONE
            }

            val restOfLog = log.substring(result.range.last + 1).trim()
            logLevel to restOfLog
        }
    }
}

private const val LOGGER_TIMESTAMP = "hh:mm:ss:SSS"