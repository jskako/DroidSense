package adb

import notifications.ExtendedLog

data class DeviceDetails(
    val serialNumber: String,
    val model: String? = null,
    val manufacturer: String? = null,
    val brand: String? = null,
    val buildSDK: String? = null,
    val androidVersion: String? = null,
    val displayResolution: String? = null,
    val displayDensity: String? = null,
    val ipAddress: String? = null,
    var state: AdbDeviceStatus? = null,
    val extra: ExtendedLog? = null
) {
    override fun toString(): String {
        return "$model ($serialNumber)"
    }
}