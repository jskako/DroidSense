package adb

import notifications.ExtendedLog

data class DeviceDetails(
    val serialNumber: String,
    val model: String,
    val manufacturer: String,
    val brand: String,
    val buildSDK: String,
    val androidVersion: String,
    val displayResolution: String,
    val displayDensity: String,
    val ipAddress: String,
    var state: AdbDeviceStatus,
    val extra: ExtendedLog? = null
) {
    override fun toString(): String {
        return "$model ($serialNumber)"
    }
}