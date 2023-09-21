package adb

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.graphics.Color
import notifications.InfoManager.showInfoMessage
import utils.DEVICE_MODEL_PROPERTY
import utils.EMPTY_STRING
import utils.getStringResource

object DeviceManager : DeviceManagerInterface {

    private val _devices = mutableStateListOf<DeviceDetails>()

    override suspend fun addDevice(serialNumber: String) {
        val model = getDeviceProperty(serialNumber, DEVICE_MODEL_PROPERTY)
        val newDevice = DeviceDetails(serialNumber, model, AdbDeviceStatus.CONNECTED)
        _devices.add(newDevice)
        showInfoMessage("${getStringResource("info.add.device")}: $newDevice")
    }

    override suspend fun removeDevice(serialNumber: String) {
        val deviceToRemove = _devices.firstOrNull { it.serialNumber == serialNumber }
        deviceToRemove?.let { device ->
            _devices.remove(device)
            showInfoMessage(
                message = "${getStringResource("info.remove.device")}: $device",
                backgroundColor = Color.Red
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

    val devices: List<DeviceDetails> get() = _devices

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
