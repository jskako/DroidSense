package adb

import kotlinx.coroutines.CoroutineScope

interface AdbDeviceManagerInterface {
    fun startListening(coroutineScope: CoroutineScope)
    fun stopListening(coroutineScope: CoroutineScope)
    fun manageListeningStatus(coroutineScope: CoroutineScope)
}