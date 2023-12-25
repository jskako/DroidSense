package adb

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import java.io.BufferedReader
import java.io.InputStreamReader
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import notifications.InfoManager.showInfoMessage
import notifications.InfoManager.showTimeLimitedInfoMessage
import settitngs.GlobalVariables.adbPath
import utils.ADB_POLLING_INTERVAL_MS
import utils.Colors
import utils.getStringResource

object AdbDeviceManager : AdbDeviceManagerInterface {

    private var monitorJob: Job? = null
    private val _listeningStatus = mutableStateOf(ListeningStatus.NOT_LISTENING)

    val listeningStatus: State<ListeningStatus>
        get() = _listeningStatus

    override fun startListening(
        deviceManager: DeviceManager,
        coroutineScope: CoroutineScope
    ) {
        monitorJob?.cancel()
        monitorJob = coroutineScope.launch {
            deviceManager.clearDevices()
            _listeningStatus.value = ListeningStatus.LISTENING
            monitorAdbDevices(
                deviceManager = deviceManager
            )
        }
    }

    override fun stopListening(
        deviceManager: DeviceManager,
        coroutineScope: CoroutineScope
    ) {
        coroutineScope.launch {
            monitorJob?.cancel()
            _listeningStatus.value = ListeningStatus.NOT_LISTENING
            deviceManager.updateDevicesStatus()
        }
    }

    override fun manageListeningStatus(
        deviceManager: DeviceManager,
        coroutineScope: CoroutineScope
    ) {
        if (_listeningStatus.value == ListeningStatus.LISTENING) {
            stopListening(
                deviceManager = deviceManager,
                coroutineScope = coroutineScope
            )
            showTimeLimitedInfoMessage(getStringResource("info.stop.listening"))
        } else {
            startListening(
                deviceManager = deviceManager,
                coroutineScope = coroutineScope
            )
            showTimeLimitedInfoMessage(getStringResource("info.start.listening"))
        }
    }

    private suspend fun monitorAdbDevices(deviceManager: DeviceManager) {
        withContext(Default) {
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
                                if (!deviceManager.devices.any { it.serialNumber == serialNumber }) {
                                    deviceManager.addDevice(serialNumber)
                                }
                            }
                        }
                    }

                    handleDisconnectedDevices(
                        deviceManager = deviceManager,
                        currentDevices = currentDevices
                    )

                }.getOrElse { exception ->
                    showInfoMessage(
                        "${getStringResource("error.monitor.general")}: $exception",
                        backgroundColor = Colors.darkRed
                    )
                }

                delay(ADB_POLLING_INTERVAL_MS)
            }
        }
    }

    private suspend fun handleDisconnectedDevices(
        deviceManager: DeviceManager,
        currentDevices: Set<String>
    ) {
        if (listeningStatus.value == ListeningStatus.LISTENING) {
            val disconnectedSerialNumbers = deviceManager.devices.map { it.serialNumber } - currentDevices
            for (serialNumber in disconnectedSerialNumbers) {
                deviceManager.removeDevice(serialNumber)
            }
        }
    }
}

