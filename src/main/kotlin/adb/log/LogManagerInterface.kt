package adb.log

import data.model.items.LogItem
import kotlinx.coroutines.CoroutineScope
import notifications.InfoManagerData
import java.util.UUID

interface LogManagerInterface {
    suspend fun startMonitoring(
        coroutineScope: CoroutineScope,
        packageName: String,
        identifier: String,
        serialNumber: String,
        onMessage: (InfoManagerData) -> Unit,
        onUuidCreated: (UUID) -> Unit,
        onLastLog: (LogItem) -> Unit
    )

    suspend fun stopMonitoring()
    suspend fun clear(identifier: String)
}