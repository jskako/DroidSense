package log

import kotlinx.coroutines.CoroutineScope
import notifications.InfoManagerData

interface LogManagerInterface {
    suspend fun startMonitoringLogs(
        coroutineScope: CoroutineScope,
        packageName: String,
        identifier: String,
        onMessage: (InfoManagerData) -> Unit
    )

    suspend fun stopMonitoringLogs()
    suspend fun clearLogs()
}