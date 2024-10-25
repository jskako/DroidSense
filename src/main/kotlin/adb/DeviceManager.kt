package adb

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import notifications.InfoManagerData
import utils.ADB_DEVICE_OFFLINE
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
import utils.DEVICE_SERIAL_NUMBER
import utils.deviceRegex
import utils.getStringResource
import java.io.BufferedReader
import java.io.InputStreamReader

class DeviceManager(
    private val adbPath: String
) : DeviceManagerInterface {

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
                adbPath = adbPath,
                onMessage = onMessage,
                scope = coroutineScope
            )
        }
    }

    private fun stopListening(
        coroutineScope: CoroutineScope
    ) {
        coroutineScope.launch {
            monitorJob?.cancel()
            _monitoringStatus.value = MonitoringStatus.NOT_MONITORING
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
        adbPath: String,
        onMessage: (InfoManagerData) -> Unit,
        scope: CoroutineScope
    ) {
        withContext(Default) {
            while (true) {
                val currentDevices = mutableSetOf<String>()

                runCatching {
                    val process = ProcessBuilder(adbPath, "devices", "-l").start()
                    val reader = BufferedReader(InputStreamReader(process.inputStream))

                    reader.useLines { lines ->
                        lines.forEach { line ->
                            if (line.matches(deviceRegex)) {

                                val parts = line.split("\\s+".toRegex())
                                val identifier = parts.first().trim()
                                val deviceExist = devices.any { it.deviceIdentifier == identifier }
                                val deviceAvailable = parts.last().trim() != ADB_DEVICE_OFFLINE

                                currentDevices.add(identifier)

                                if (!deviceExist && deviceAvailable) {
                                    addDevice(
                                        identifier = identifier,
                                        onMessage = onMessage
                                    )
                                }

                                if (deviceExist && deviceAvailable) {
                                    updateDevice(
                                        identifier = identifier,
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
                    stopListening(
                        coroutineScope = scope
                    )
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

    private fun handleDisconnectedDevices(
        currentDevices: Set<String>,
        onMessage: (InfoManagerData) -> Unit
    ) {
        if (monitoringStatus.value == MonitoringStatus.MONITORING) {
            val disconnectedIdentifiers = devices.map { it.deviceIdentifier } - currentDevices
            for (identifier in disconnectedIdentifiers) {
                removeDevice(
                    identifier = identifier,
                    onMessage = onMessage
                )
            }
        }
    }

    private fun addDevice(
        identifier: String,
        onMessage: (InfoManagerData) -> Unit
    ) {
        getDeviceInfo(
            identifier = identifier
        ).also {
            _devices.add(it)
            onMessage(InfoManagerData(message = "${getStringResource("info.add.device")}: $it"))
        }
    }

    private fun updateDevice(
        identifier: String,
        onMessage: (InfoManagerData) -> Unit
    ) {
        getDeviceInfo(
            identifier = identifier
        ).also { deviceDetails ->
            _devices.indexOfFirst { it.deviceIdentifier == deviceDetails.deviceIdentifier }.also { index ->
                if (_devices[index] != deviceDetails) {
                    _devices[index] = deviceDetails
                    onMessage(InfoManagerData(message = "${getStringResource("info.update.device")}: $deviceDetails"))
                }
            }
        }
    }

    private fun getDeviceInfo(
        identifier: String
    ) = DeviceDetails(
        deviceIdentifier = identifier,
        serialNumber = getDeviceProperty(
            identifier = identifier,
            property = DEVICE_SERIAL_NUMBER,
            adbPath = adbPath
        ),
        model = getDeviceProperty(
            identifier = identifier,
            property = DEVICE_MODEL_PROPERTY,
            adbPath = adbPath
        ),
        manufacturer = getDeviceProperty(
            identifier = identifier,
            property = DEVICE_MANUFACTURER,
            adbPath = adbPath
        ),
        brand = getDeviceProperty(
            identifier = identifier,
            property = DEVICE_BRAND,
            adbPath = adbPath
        ),
        buildSDK = getDeviceProperty(
            identifier = identifier,
            property = DEVICE_BUILD_SDK,
            adbPath = adbPath
        ),
        androidVersion = getDeviceProperty(
            identifier = identifier,
            property = DEVICE_ANDROID_VERSION,
            adbPath = adbPath
        ),
        displayResolution = getDeviceProperty(
            identifier = identifier,
            property = DEVICE_DISPLAY_RESOLUTION,
            adbPath = adbPath
        ),
        displayDensity = getDeviceProperty(
            identifier = identifier,
            property = DEVICE_DISPLAY_DENSITY,
            adbPath = adbPath
        ),
        ipAddress = getDeviceProperty(
            identifier = identifier,
            property = DEVICE_IP_ADDRESS,
            adbPath = adbPath
        ).takeIf { it.isNotEmpty() }
            ?: getStringResource("info.not.connected"),
        state = MonitoringStatus.MONITORING,
        privateSpaceIdentifier = getPrivateSpaceId(
            adbPath = adbPath,
            identifier = identifier
        )
    )

    private fun removeDevice(
        identifier: String,
        onMessage: (InfoManagerData) -> Unit
    ) {
        val deviceToRemove = _devices.firstOrNull { it.deviceIdentifier == identifier }
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
}

enum class MonitorStatus {
    START, STOP
}

