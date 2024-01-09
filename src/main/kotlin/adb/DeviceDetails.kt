package adb

import utils.capitalizeFirstChar

data class DeviceDetails(
    val deviceIdentifier: String,
    val serialNumber: String,
    val model: String? = null,
    val manufacturer: String? = null,
    val brand: String? = null,
    val buildSDK: String? = null,
    val androidVersion: String? = null,
    val displayResolution: String? = null,
    val displayDensity: String? = null,
    val ipAddress: String? = null,
    var state: MonitoringStatus? = null,
) {
    override fun toString(): String {
        return "${manufacturer?.capitalizeFirstChar()} $model ($serialNumber)"
    }
}