package adb

import logs.ExtendedLog

data class DeviceDetails(
    val serialNumber: String,
    val model: String,
    var state: AdbDeviceStatus,
    val extra: ExtendedLog? = null
) {
    override fun toString(): String {
        return "$model ($serialNumber)"
    }
}