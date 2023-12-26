package adb

import kotlinx.coroutines.CoroutineScope
import notifications.InfoManagerData

interface AdbDeviceManagerInterface {
    fun manageListeningStatus(
        monitorStatus: MonitorStatus,
        deviceManager: DeviceManager,
        scope: CoroutineScope,
        onMessage: (InfoManagerData) -> Unit
    )

    fun isMonitoring(): Boolean
}