package adb

import kotlinx.coroutines.CoroutineScope
import notifications.InfoManagerData

interface DeviceManagerInterface {
    fun manageListeningStatus(
        monitorStatus: MonitorStatus,
        scope: CoroutineScope,
        onMessage: (InfoManagerData) -> Unit
    )

    fun isMonitoring(): Boolean
}