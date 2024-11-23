package adb

import data.model.items.DeviceItem
import kotlinx.coroutines.CoroutineScope
import notifications.InfoManagerData

interface DeviceManagerInterface {
    fun manageListeningStatus(
        monitorStatus: MonitorStatus,
        scope: CoroutineScope,
        onMessage: (InfoManagerData) -> Unit,
        onDeviceFound: (DeviceItem) -> Unit
    )

    fun isMonitoring(): Boolean
}