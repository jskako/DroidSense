package adb

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import notifications.InfoManager.showTimeLimitedInfoMessage
import utils.Colors.darkRed
import utils.DEVICE_ANDROID_VERSION
import utils.DEVICE_BRAND
import utils.DEVICE_BUILD_SDK
import utils.DEVICE_DISPLAY_DENSITY
import utils.DEVICE_DISPLAY_RESOLUTION
import utils.DEVICE_IP_ADDRESS
import utils.DEVICE_MANUFACTURER
import utils.DEVICE_MODEL_PROPERTY
import utils.EMPTY_STRING
import utils.getStringResource

object DeviceManager : DeviceManagerInterface {

    private val _devices = mutableStateListOf<DeviceDetails>()

    override suspend fun addDevice(serialNumber: String) {
        val newDevice = DeviceDetails(
            serialNumber = serialNumber,
            model = getDeviceProperty(serialNumber, DEVICE_MODEL_PROPERTY),
            manufacturer = getDeviceProperty(serialNumber, DEVICE_MANUFACTURER),
            brand = getDeviceProperty(serialNumber, DEVICE_BRAND),
            buildSDK = getDeviceProperty(serialNumber, DEVICE_BUILD_SDK),
            androidVersion = getDeviceProperty(serialNumber, DEVICE_ANDROID_VERSION),
            displayResolution = getDeviceProperty(serialNumber, DEVICE_DISPLAY_RESOLUTION),
            displayDensity = getDeviceProperty(serialNumber, DEVICE_DISPLAY_DENSITY),
            ipAddress = getDeviceProperty(serialNumber, DEVICE_IP_ADDRESS),
            state = AdbDeviceStatus.CONNECTED
        )
        _devices.add(newDevice)
        showTimeLimitedInfoMessage("${getStringResource("info.add.device")}: $newDevice")
    }

    override suspend fun removeDevice(serialNumber: String) {
        val deviceToRemove = _devices.firstOrNull { it.serialNumber == serialNumber }
        deviceToRemove?.let { device ->
            _devices.remove(device)
            showTimeLimitedInfoMessage(
                message = "${getStringResource("info.remove.device")}: $device",
                backgroundColor = darkRed
            )
        }
    }

    override fun clearDevices() {
        _devices.clear()
    }

    override suspend fun updateDevicesStatus(status: AdbDeviceStatus) {
        _devices.forEachIndexed { index, device ->
            _devices[index] = device.copy(state = status)
        }
    }

    var devices by mutableStateOf<List<DeviceDetails>>(_devices)
        private set

    private fun getDeviceProperty(serialNumber: String, property: String): String {
        return runCatching {
            ProcessBuilder("adb", "-s", serialNumber, "shell", property).start().inputStream
                .bufferedReader()
                .use { reader ->
                    reader.readLine()
                }
        }.getOrElse {
            EMPTY_STRING
        }
    }
}
