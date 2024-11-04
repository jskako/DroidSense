package adb

import data.model.items.PhoneItem
import kotlinx.coroutines.CoroutineScope
import notifications.InfoManagerData

interface DeviceManagerInterface {
    fun manageListeningStatus(
        monitorStatus: MonitorStatus,
        scope: CoroutineScope,
        onMessage: (InfoManagerData) -> Unit,
        onDeviceFound: (PhoneItem) -> Unit
    )

    fun isMonitoring(): Boolean
}