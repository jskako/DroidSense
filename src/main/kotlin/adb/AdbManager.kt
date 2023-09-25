package adb

import adb.DeviceManager.addDevice
import adb.DeviceManager.clearDevices
import adb.DeviceManager.devices
import adb.DeviceManager.removeDevice
import adb.DeviceManager.updateDevicesStatus
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import notifications.InfoManager.showInfoMessage
import notifications.InfoManager.showTimeLimitedInfoMessage
import settitngs.GlobalSettings.adbPath
import utils.ADB_POLLING_INTERVAL_MS
import utils.Colors
import utils.getStringResource
import java.io.BufferedReader
import java.io.InputStreamReader

object AdbManager : AdbManagerInterface {

    private var monitorJob: Job? = null
    private val _listeningStatus = mutableStateOf(ListeningStatus.NOT_LISTENING)

    val listeningStatus: State<ListeningStatus>
        get() = _listeningStatus

    override fun startListening(coroutineScope: CoroutineScope) {
        monitorJob?.cancel()
        monitorJob = coroutineScope.launch {
            clearDevices()
            _listeningStatus.value = ListeningStatus.LISTENING
            monitorAdbDevices()
        }
    }

    override fun stopListening(coroutineScope: CoroutineScope) {
        coroutineScope.launch {
            monitorJob?.cancel()
            _listeningStatus.value = ListeningStatus.NOT_LISTENING
            updateDevicesStatus()
        }
    }

    override fun manageListeningStatus(coroutineScope: CoroutineScope) {
        if (_listeningStatus.value == ListeningStatus.LISTENING) {
            stopListening(coroutineScope)
            showTimeLimitedInfoMessage("Stop Listening")
        } else {
            startListening(coroutineScope)
            showTimeLimitedInfoMessage("Start Listening")
        }
    }

    private suspend fun monitorAdbDevices() {

        while (true) {
            val currentDevices = mutableSetOf<String>()

            runCatching {
                val process = ProcessBuilder(adbPath.value, "devices", "-l").start()
                val reader = BufferedReader(InputStreamReader(process.inputStream))

                reader.useLines { lines ->
                    lines.forEach { line ->
                        if (line.matches(Regex(".*device\\s+.*"))) {
                            val parts = line.split("\\s+".toRegex())
                            val serialNumber = parts[0].trim()
                            currentDevices.add(serialNumber)
                            handleNewDevice(serialNumber)
                        }
                    }
                }

                handleDisconnectedDevices(currentDevices)

            }.getOrElse { exception ->
                showInfoMessage(
                    "${getStringResource("error.monitor.general")}: $exception",
                    backgroundColor = Colors.darkRed
                )
            }

            delay(ADB_POLLING_INTERVAL_MS)
        }
    }

    private suspend fun handleNewDevice(serialNumber: String) {
        if (serialNumber !in devices.map { it.serialNumber }) {
            addDevice(serialNumber)
        }
    }

    private suspend fun handleDisconnectedDevices(currentDevices: Set<String>) {
        if (listeningStatus.value == ListeningStatus.LISTENING) {
            val disconnectedSerialNumbers = devices.map { it.serialNumber } - currentDevices
            for (serialNumber in disconnectedSerialNumbers) {
                removeDevice(serialNumber)
            }
        }
    }
}

