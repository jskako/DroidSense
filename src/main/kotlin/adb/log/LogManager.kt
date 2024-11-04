package adb.log

import androidx.compose.runtime.mutableStateListOf
import data.model.LogItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import notifications.ExportData
import notifications.InfoManagerData
import utils.Colors.darkRed
import utils.EMPTY_STRING
import utils.LOG_MANAGER_NUMBER_OF_LINES
import utils.copyToClipboard
import utils.exportToFile
import utils.getStringResource
import utils.runCommand
import java.util.UUID


class LogManager(
    private val adbPath: String
) : LogManagerInterface {

    private var monitorJob: Job? = null
    private val _logs = mutableStateListOf<LogItem>()
    private var currentProcess: Process? = null

    val logs: List<LogItem>
        get() = _logs

    val isActive: Boolean
        get() = monitorJob?.isActive == true

    suspend fun export(
        exportOption: ExportOption = ExportOption.ALL,
        onExportDone: (exportData: ExportData) -> Unit
    ) {
        withContext(Dispatchers.IO) {
            onExportDone(buildString(exportOption).exportToFile())
        }
    }

    suspend fun copy(
        exportOption: ExportOption = ExportOption.ALL
    ) {
        withContext(Dispatchers.Default) {
            buildString(exportOption).copyToClipboard()
        }
    }

    suspend fun isSelected(
        selectOption: SelectOption,
        onSelectDone: () -> Unit
    ) {
        val isSelectedValue = when (selectOption) {
            SelectOption.SELECT -> true
            SelectOption.DESELECT -> false
        }

        withContext(Dispatchers.Default) {
            _logs.forEachIndexed { index, log ->
                if (log.isSelected != isSelectedValue) {
                    _logs[index] = log.copy(isSelected = isSelectedValue)
                }
            }
            onSelectDone()
        }
    }

    private fun buildString(exportOption: ExportOption): String {
        return buildString {
            val logsToExport = when (exportOption) {
                ExportOption.ALL -> logs
                ExportOption.SELECTED -> getSelected()
            }

            logsToExport.forEach { log ->
                appendLine(log.toString())
            }
        }
    }

    private fun getSelected() = logs.filter { it.isSelected }

    fun isSelected(uuid: UUID) {
        runCatching {
            val logData = _logs.find { it.uuid == uuid } ?: throw NoSuchElementException("Log entry not found")
            val index = _logs.indexOf(logData)
            _logs[index] = logData.copy(isSelected = !logData.isSelected)
        }.onFailure { exception ->
        }
    }

    override suspend fun startMonitoring(
        coroutineScope: CoroutineScope,
        packageName: String,
        identifier: String,
        onMessage: (InfoManagerData) -> Unit
    ) {
        stopMonitoring()
        clear()
        monitorJob = coroutineScope.launch {
            runCatching {
                monitor(
                    packageName = packageName.takeUnless { it == getStringResource("info.log.starting.package") },
                    identifier = identifier,
                    onMessage = onMessage
                )
            }.onFailure { e ->
                onMessage(
                    InfoManagerData(
                        message = e.message ?: EMPTY_STRING,
                        color = darkRed
                    )
                )
            }
        }
    }

    override suspend fun stopMonitoring() {
        monitorJob?.cancel()
        currentProcess?.destroy()
    }

    override suspend fun clear() {
        clearAdbCache()
        _logs.clear()
    }

    private suspend fun monitor(
        packageName: String?,
        identifier: String,
        onMessage: (InfoManagerData) -> Unit
    ) {
        withContext(Dispatchers.IO) {
            var pid = EMPTY_STRING
            val uuid = UUID.randomUUID()
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
                    clear()
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

                            val components = line.split("\\s+".toRegex(), 6)
                            if (components.size < 6) return@forEach

                            val (tag, text) = components.getOrNull(5)?.split(":", limit = 2)?.let {
                                it.getOrElse(0) { "" }.trim() to it.getOrElse(1) { "" }.trim()
                            } ?: ("" to "")

                            _logs.add(
                                LogItem(
                                    uuid = uuid,
                                    date = components.getOrElse(index = 0, defaultValue = { "" }),
                                    time = components.getOrElse(index = 1, defaultValue = { "" }),
                                    pid = components.getOrNull(index = 2)?.toLongOrNull() ?: 0L,
                                    tid = components.getOrNull(index = 3)?.toLongOrNull() ?: 0L,
                                    level = components.getOrNull(4)?.let {
                                        when (it.first()) {
                                            LogLevel.INFO.simplified() -> LogLevel.INFO
                                            LogLevel.ERROR.simplified() -> LogLevel.ERROR
                                            LogLevel.WARN.simplified() -> LogLevel.WARN
                                            LogLevel.VERBOSE.simplified() -> LogLevel.VERBOSE
                                            LogLevel.DEBUG.simplified() -> LogLevel.DEBUG
                                            else -> LogLevel.NONE
                                        }
                                    } ?: LogLevel.NONE,
                                    tag = tag,
                                    text = text,
                                )
                            )
                        }

                        if (_logs.size > LOG_MANAGER_NUMBER_OF_LINES) {
                            _logs.removeFirst()
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

    private fun clearAdbCache() {
        "$adbPath logcat -c".runCommand()
    }

    private fun getPid(
        identifier: String,
        packageName: String
    ) =
        "$adbPath -s $identifier shell pidof -s $packageName".runCommand()?.trim() ?: EMPTY_STRING
}