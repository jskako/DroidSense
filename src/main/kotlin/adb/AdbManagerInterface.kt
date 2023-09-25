package adb

import kotlinx.coroutines.CoroutineScope

interface AdbManagerInterface {
    fun startListening(coroutineScope: CoroutineScope)
    fun stopListening(coroutineScope: CoroutineScope)
    fun manageListeningStatus(coroutineScope: CoroutineScope)
}