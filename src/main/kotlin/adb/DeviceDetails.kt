package adb

import notifications.ExtendedLog
import ui.composable.elements.device.DeviceCardInfo
import utils.capitalizeFirstChar
import utils.getStringResource

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

    fun toDeviceCardInfoList(): List<DeviceCardInfo> {
        val propertyList = listOf(
            "info.serial.number" to serialNumber,
            ("info.model" to model),
            ("info.manufacturer" to manufacturer?.capitalizeFirstChar()),
            ("info.brand" to brand?.capitalizeFirstChar()),
            ("info.build.sdk" to buildSDK),
            ("info.android.version" to androidVersion),
            ("info.display.resolution" to (displayResolution?.split(": ")?.get(1) ?: "")),
            ("info.display.density" to (displayDensity?.split(": ")?.get(1) ?: "")),
            ("info.ip.address" to ipAddress)
        )

        return propertyList.map { (descriptionKey, value) ->
            DeviceCardInfo(description = getStringResource(descriptionKey), value = value ?: "")
        }
    }
}