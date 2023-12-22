package log

import androidx.compose.runtime.mutableStateListOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import notifications.InfoManager.showTimeLimitedInfoMessage
import settitngs.GlobalVariables.adbPath
import utils.LOG_TYPE_REGEX
import utils.getStringResource
import utils.getTimeStamp
import utils.runCommand

class LogManager : LogManagerInterface {

    private var monitorJob: Job? = null
    private val _logs = mutableStateListOf<LogData>()
    private var currentProcess: Process? = null

    val logs: List<LogData>
        get() = _logs

    val isActive: Boolean
        get() = monitorJob?.isActive == true

    override suspend fun startMonitoringLogs(
        coroutineScope: CoroutineScope,
        packageName: String,
        serialNumber: String
    ) {
        stopMonitoringLogs()
        _logs.clear()
        monitorJob = coroutineScope.launch {
            try {
                monitorLogs(
                    packageName = if (packageName == getStringResource("info.log.starting.package")) null else packageName,
                    serialNumber = serialNumber
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

    private suspend fun monitorLogs(packageName: String?, serialNumber: String) {
        withContext(Dispatchers.IO) {
            var pid = ""
            if (!packageName.isNullOrEmpty()) {
                pid = getPid(packageName)

                if (pid.isEmpty()) {
                    _logs.add(
                        LogData(
                            time = getTimeStamp(LOGGER_TIMESTAMP),
                            log = getStringResource("info.app.not.running"),
                            level = LogLevel.NONE
                        )
                    )

                    while (pid.isEmpty()) {
                        delay(1000)
                        LogData(
                            time = getTimeStamp(LOGGER_TIMESTAMP),
                            log = getStringResource("info.checking.app.online"),
                            level = LogLevel.NONE
                        )
                        pid = getPid(packageName)
                    }

                    _logs.clear()
                }
            }

            val logcatProcess = ProcessBuilder(adbPath.value, "logcat").apply {
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
                showTimeLimitedInfoMessage("${getStringResource("info.log.error")} $serialNumber: $exception")
            }
        }
    }

    private fun getPid(packageName: String) = "${adbPath.value} shell pidof -s $packageName".runCommand()?.trim() ?: ""

    private fun extractLogInfo(log: String): Pair<LogLevel, String>? {
        val logLevelsRegex = Regex("\\b[$LOG_TYPE_REGEX]\\b")
        val matchResult = logLevelsRegex.find(log)

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