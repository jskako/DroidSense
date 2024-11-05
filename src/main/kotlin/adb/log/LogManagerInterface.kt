package adb.log

import kotlinx.coroutines.CoroutineScope
import notifications.InfoManagerData

interface LogManagerInterface {
    suspend fun startMonitoring(
        coroutineScope: CoroutineScope,
        packageName: String,
        identifier: String,
        serialNumber: String,
        onMessage: (InfoManagerData) -> Unit
    )

    suspend fun stopMonitoring()
    suspend fun clear(identifier: String)
}