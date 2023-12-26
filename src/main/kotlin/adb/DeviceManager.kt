package adb

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
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
import utils.DEVICE_ANDROID_VERSION
import utils.DEVICE_BRAND
import utils.DEVICE_BUILD_SDK
import utils.DEVICE_DISPLAY_DENSITY
import utils.DEVICE_DISPLAY_RESOLUTION
import utils.DEVICE_IP_ADDRESS
import utils.DEVICE_MANUFACTURER
import utils.DEVICE_MODEL_PROPERTY
import utils.getDeviceProperty
import utils.getStringResource

class DeviceManager : DeviceManagerInterface {

    private var monitorJob: Job? = null
    private val _devices = mutableStateListOf<DeviceDetails>()
    private val _monitoringStatus = mutableStateOf(MonitoringStatus.NOT_MONITORING)
    val devices: List<DeviceDetails>
        get() = _devices

    val monitoringStatus: State<MonitoringStatus>
        get() = _monitoringStatus

    private fun startListening(
        onMessage: (InfoManagerData) -> Unit,
        coroutineScope: CoroutineScope
    ) {
        monitorJob?.cancel()
        monitorJob = coroutineScope.launch {
            clearDevices()
            _monitoringStatus.value = MonitoringStatus.MONITORING
            monitorAdbDevices(
                onMessage = onMessage
            )
        }
    }

    private fun stopListening(
        coroutineScope: CoroutineScope
    ) {
        coroutineScope.launch {
            monitorJob?.cancel()
            _monitoringStatus.value = MonitoringStatus.NOT_MONITORING
            updateDevicesStatus(MonitoringStatus.NOT_MONITORING)
        }
    }

    override fun manageListeningStatus(
        monitorStatus: MonitorStatus,
        scope: CoroutineScope,
        onMessage: (InfoManagerData) -> Unit
    ) {
        if (_monitoringStatus.value == MonitoringStatus.MONITORING) {
            stopListening(
                coroutineScope = scope
            )
            onMessage(
                InfoManagerData(
                    message = getStringResource("info.stop.listening")
                )
            )
        } else {
            startListening(
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
                                if (!devices.any { it.serialNumber == serialNumber }) {
                                    addDevice(
                                        serialNumber = serialNumber,
                                        onMessage = onMessage
                                    )
                                }
                            }
                        }
                    }

                    handleDisconnectedDevices(
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
        currentDevices: Set<String>,
        onMessage: (InfoManagerData) -> Unit
    ) {
        if (monitoringStatus.value == MonitoringStatus.MONITORING) {
            val disconnectedSerialNumbers = devices.map { it.serialNumber } - currentDevices
            for (serialNumber in disconnectedSerialNumbers) {
                removeDevice(
                    serialNumber = serialNumber,
                    onMessage = onMessage
                )
            }
        }
    }

    private fun addDevice(
        serialNumber: String,
        onMessage: (InfoManagerData) -> Unit
    ) {
        // TODO: Update details if changed
        val newDevice = DeviceDetails(
            serialNumber = serialNumber,
            model = getDeviceProperty(serialNumber, DEVICE_MODEL_PROPERTY),
            manufacturer = getDeviceProperty(serialNumber, DEVICE_MANUFACTURER),
            brand = getDeviceProperty(serialNumber, DEVICE_BRAND),
            buildSDK = getDeviceProperty(serialNumber, DEVICE_BUILD_SDK),
            androidVersion = getDeviceProperty(serialNumber, DEVICE_ANDROID_VERSION),
            displayResolution = getDeviceProperty(serialNumber, DEVICE_DISPLAY_RESOLUTION),
            displayDensity = getDeviceProperty(serialNumber, DEVICE_DISPLAY_DENSITY),
            ipAddress = getDeviceProperty(serialNumber, DEVICE_IP_ADDRESS).takeIf { it.isNotEmpty() }
                ?: getStringResource("info.not.connected"),
            state = MonitoringStatus.MONITORING
        )
        _devices.add(newDevice)
        onMessage(InfoManagerData(message = "${getStringResource("info.add.device")}: $newDevice"))
    }

    private fun removeDevice(
        serialNumber: String,
        onMessage: (InfoManagerData) -> Unit
    ) {
        val deviceToRemove = _devices.firstOrNull { it.serialNumber == serialNumber }
        deviceToRemove?.let { device ->
            _devices.remove(device)
            onMessage(
                InfoManagerData(
                    message = "${getStringResource("info.remove.device")}: $device",
                    color = darkRed
                ),
            )
        }
    }

    private fun clearDevices() {
        _devices.clear()
    }

    private fun updateDevicesStatus(status: MonitoringStatus) {
        _devices.forEachIndexed { index, device ->
            _devices[index] = device.copy(state = status)
        }
    }
}

enum class MonitorStatus {
    START, STOP
}

