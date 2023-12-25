package adb

import kotlinx.coroutines.CoroutineScope

interface AdbDeviceManagerInterface {
    fun startListening(deviceManager: DeviceManager, coroutineScope: CoroutineScope)
    fun stopListening(deviceManager: DeviceManager, coroutineScope: CoroutineScope)
    fun manageListeningStatus(deviceManager: DeviceManager, coroutineScope: CoroutineScope)
}