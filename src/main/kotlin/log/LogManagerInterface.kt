package log

import kotlinx.coroutines.CoroutineScope

interface LogManagerInterface {
    suspend fun startMonitoringLogs(
        coroutineScope: CoroutineScope,
        packageName: String,
        serialNumber: String
    )

    suspend fun stopMonitoringLogs()
}