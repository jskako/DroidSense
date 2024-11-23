package adb.log

import data.model.items.LogItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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
    private var currentProcess: Process? = null

    private val _logs = MutableStateFlow<List<LogItem>>(emptyList())
    val logs: StateFlow<List<LogItem>> = _logs

    val isActive: Boolean
        get() = monitorJob?.isActive == true

    private fun addLog(log: LogItem) {
        _logs.value += log
    }

    private fun removeFirstLog() {
        _logs.value = _logs.value.drop(1)
    }

    private fun clearLogs() {
        _logs.value = emptyList()
    }

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
            val updatedLogs = _logs.value.map { log ->
                if (log.isSelected != isSelectedValue) {
                    log.copy(isSelected = isSelectedValue)
                } else {
                    log
                }
            }

            _logs.value = updatedLogs

            onSelectDone()
        }
    }

    private fun buildString(exportOption: ExportOption): String {
        return buildString {
            val logsToExport = when (exportOption) {
                ExportOption.ALL -> logs.value
                ExportOption.SELECTED -> getSelected()
            }

            logsToExport.forEach { log ->
                appendLine(log.toString())
            }
        }
    }

    private fun getSelected(): List<LogItem> = logs.value.filter { it.isSelected }

    fun isSelected(uuid: UUID) {
        val updatedLogs = _logs.value.map { logItem ->
            if (logItem.itemUuid == uuid) {
                logItem.copy(isSelected = !logItem.isSelected)
            } else {
                logItem
            }
        }
        _logs.value = updatedLogs
    }

    override suspend fun startMonitoring(
        coroutineScope: CoroutineScope,
        packageName: String,
        identifier: String,
        serialNumber: String,
        onMessage: (InfoManagerData) -> Unit,
        onUuidCreated: (UUID) -> Unit,
        onLastLog: (LogItem) -> Unit
    ) {
        stopMonitoring()
        clear(identifier = identifier)
        monitorJob = coroutineScope.launch {
            runCatching {
                monitor(
                    packageName = packageName.takeUnless { it == getStringResource("info.log.starting.package") },
                    identifier = identifier,
                    serialNumber = serialNumber,
                    onMessage = onMessage,
                    onUuidCreated = onUuidCreated,
                    onLastLog = onLastLog
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

    override suspend fun clear(identifier: String) {
        clearAdbCache(identifier = identifier)
        clearLogs()
    }

    private suspend fun monitor(
        packageName: String?,
        identifier: String,
        serialNumber: String,
        onMessage: (InfoManagerData) -> Unit,
        onUuidCreated: (UUID) -> Unit,
        onLastLog: (LogItem) -> Unit
    ) {
        withContext(Dispatchers.IO) {
            var pid = EMPTY_STRING
            val uuid = UUID.randomUUID().also(onUuidCreated)

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
                    clear(identifier = identifier)
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

                            LogItem(
                                sessionUuid = uuid,
                                itemUuid = UUID.randomUUID(),
                                deviceSerialNumber = serialNumber,
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
                            ).also {
                                addLog(it)
                                onLastLog(it)
                            }
                        }

                        if (_logs.value.size > LOG_MANAGER_NUMBER_OF_LINES) {
                            removeFirstLog()
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

    private fun clearAdbCache(identifier: String) {
        ("$adbPath -s $identifier logcat -c").runCommand()
    }

    private fun getPid(
        identifier: String,
        packageName: String
    ) =
        "$adbPath -s $identifier shell pidof -s $packageName".runCommand()?.trim() ?: EMPTY_STRING
}