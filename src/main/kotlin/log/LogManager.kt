package log

import androidx.compose.runtime.mutableStateListOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import notifications.InfoManager.showTimeLimitedInfoMessage
import settitngs.GlobalSettings.adbPath
import utils.getStringResource

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
        println("Package name: $packageName")
        monitorJob = coroutineScope.launch {
            try {
                monitorLogs(packageName, serialNumber)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override suspend fun stopMonitoringLogs() {
        monitorJob?.cancel()
        currentProcess?.destroy()
    }

    private fun monitorLogs(
        packageName: String,
        serialNumber: String
    ) {
        try {
            val process = ProcessBuilder(
                adbPath.value, "-s", serialNumber, "logcat", "-v", "thread"
            ).apply {
                if (packageName != getStringResource("info.log.starting.package")) {
                    command().add(packageName)
                }
            }.start()

            currentProcess = process

            process.inputStream.bufferedReader().useLines { lines ->
                lines.forEach { line ->
                    val text = line.split(" ").drop(3).joinToString(" ")
                    val logType = line[0].let {
                        when (it) {
                            LogLevel.INFO.simplified() -> LogLevel.INFO
                            LogLevel.ERROR.simplified() -> LogLevel.ERROR
                            LogLevel.WARNING.simplified() -> LogLevel.WARNING
                            LogLevel.VERBOSE.simplified() -> LogLevel.VERBOSE
                            LogLevel.DEBUG.simplified() -> LogLevel.DEBUG
                            else -> LogLevel.NONE
                        }
                    }
                    _logs.add(
                        LogData(
                            log = text,
                            level = logType
                        )
                    )
                }
            }
        } catch (e: Exception) {
            showTimeLimitedInfoMessage("${getStringResource("info.log.error")} $serialNumber: $e")
        }
    }
}
