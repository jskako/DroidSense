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
import notifications.InfoManagerData
import settitngs.GlobalVariables.adbPath
import utils.ADB_POLLING_INTERVAL_MS
import utils.Colors.darkRed
import utils.getStringResource

object AdbDeviceManager : AdbDeviceManagerInterface {

    private var monitorJob: Job? = null
    private val _monitoringStatus = mutableStateOf(MonitoringStatus.NOT_MONITORING)

    val monitoringStatus: State<MonitoringStatus>
        get() = _monitoringStatus

    private fun startListening(
        deviceManager: DeviceManager,
        onMessage: (InfoManagerData) -> Unit,
        coroutineScope: CoroutineScope
    ) {
        monitorJob?.cancel()
        monitorJob = coroutineScope.launch {
            deviceManager.clearDevices()
            _monitoringStatus.value = MonitoringStatus.MONITORING
            monitorAdbDevices(
                deviceManager = deviceManager,
                onMessage = onMessage
            )
        }
    }

    private fun stopListening(
        deviceManager: DeviceManager,
        coroutineScope: CoroutineScope
    ) {
        coroutineScope.launch {
            monitorJob?.cancel()
            _monitoringStatus.value = MonitoringStatus.NOT_MONITORING
            deviceManager.updateDevicesStatus()
        }
    }

    override fun manageListeningStatus(
        monitorStatus: MonitorStatus,
        deviceManager: DeviceManager,
        scope: CoroutineScope,
        onMessage: (InfoManagerData) -> Unit
    ) {
        if (_monitoringStatus.value == MonitoringStatus.MONITORING) {
            stopListening(
                deviceManager = deviceManager,
                coroutineScope = scope
            )
            onMessage(
                InfoManagerData(
                    message = getStringResource("info.stop.listening")
                )
            )
        } else {
            startListening(
                deviceManager = deviceManager,
                onMessage = onMessage,
                coroutineScope = scope
            )
            onMessage(
                InfoManagerData(
                    message = getStringResource("info.start.listening")
                )
            )
        }
    }

    override fun isMonitoring() = _monitoringStatus.value == MonitoringStatus.MONITORING

    private suspend fun monitorAdbDevices(
        deviceManager: DeviceManager,
        onMessage: (InfoManagerData) -> Unit
    ) {
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
                                    deviceManager.addDevice(
                                        serialNumber = serialNumber,
                                        onMessage = onMessage
                                    )
                                }
                            }
                        }
                    }

                    handleDisconnectedDevices(
                        deviceManager = deviceManager,
                        currentDevices = currentDevices,
                        onMessage = onMessage
                    )

                }.getOrElse { exception ->
                    onMessage(
                        InfoManagerData(
                            color = darkRed,
                            message = "${getStringResource("error.monitor.general")}: $exception",
                            duration = null
                        )
                    )
                }

                delay(ADB_POLLING_INTERVAL_MS)
            }
        }
    }

    private suspend fun handleDisconnectedDevices(
        deviceManager: DeviceManager,
        currentDevices: Set<String>,
        onMessage: (InfoManagerData) -> Unit
    ) {
        if (monitoringStatus.value == MonitoringStatus.MONITORING) {
            val disconnectedSerialNumbers = deviceManager.devices.map { it.serialNumber } - currentDevices
            for (serialNumber in disconnectedSerialNumbers) {
                deviceManager.removeDevice(
                    serialNumber = serialNumber,
                    onMessage = onMessage
                )
            }
        }
    }
}

enum class MonitorStatus {
    START, STOP
}

