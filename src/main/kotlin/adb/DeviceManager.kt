package adb

import androidx.compose.ui.graphics.Color
import di.AppModule
import utils.DEVICE_MODEL_PROPERTY
import utils.EMPTY_STRING
import utils.getStringResource

object DeviceManager : DeviceManagerInterface {

    private val _devices = mutableListOf<DeviceDetails>()
    private val infoManager = AppModule.provideInfoManager()

    override suspend fun addDevice(serialNumber: String) {
        val model = getDeviceProperty(serialNumber, DEVICE_MODEL_PROPERTY)
        DeviceDetails(serialNumber, model, AdbDeviceStatus.CONNECTED).also { device ->
            _devices.add(device)
            infoManager.showInfoMessage("${getStringResource("info.add.device")}: $device")
        }
    }

    override suspend fun removeDevice(serialNumber: String) {
        _devices.firstOrNull { it.serialNumber == serialNumber }.let { device ->
            _devices.remove(device)
            infoManager.showInfoMessage(
                message = "${getStringResource("info.remove.device")}: $device",
                backgroundColor = Color.Red
            )
        }
    }

    override fun clearDevices() {
        _devices.clear()
    }

    override suspend fun updateDevicesStatus(status: AdbDeviceStatus) {
        _devices.forEach { it.state = AdbDeviceStatus.NOT_MONITORING }
    }

    val devices: List<DeviceDetails>
        get() = _devices

    private fun getDeviceProperty(serialNumber: String, property: String): String {
        return runCatching {
            ProcessBuilder("adb", "-s", serialNumber, "shell", "getprop", property).start().inputStream
                .bufferedReader()
                .use { reader ->
                    reader.readLine()
                }
        }.getOrElse {
            EMPTY_STRING
        }
    }
}
