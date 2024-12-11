package adb

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import com.jskako.droidsense.generated.resources.Res
import com.jskako.droidsense.generated.resources.error_monitor_general
import com.jskako.droidsense.generated.resources.info_add_device
import com.jskako.droidsense.generated.resources.info_not_connected
import com.jskako.droidsense.generated.resources.info_remove_device
import com.jskako.droidsense.generated.resources.info_start_listening
import com.jskako.droidsense.generated.resources.info_stop_listening
import com.jskako.droidsense.generated.resources.info_update_device
import data.ArgsText
import data.model.items.DeviceItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import notifications.InfoManagerData
import org.jetbrains.compose.resources.getString
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
import java.io.BufferedReader
import java.io.InputStreamReader

class DeviceManager(
    private val adbPath: String
) : DeviceManagerInterface {

    private var monitorJob: Job? = null
    private val _devices = MutableStateFlow<List<DeviceDetails>>(emptyList())
    private val _monitoringStatus = mutableStateOf(MonitoringStatus.NOT_MONITORING)
    val devices = _devices.asStateFlow()

    val monitoringStatus: State<MonitoringStatus>
        get() = _monitoringStatus

    private fun startListening(
        onMessage: (InfoManagerData) -> Unit,
        coroutineScope: CoroutineScope,
        onDeviceFound: (DeviceItem) -> Unit
    ) {
        monitorJob?.cancel()
        monitorJob = coroutineScope.launch {
            clearDevices()
            _monitoringStatus.value = MonitoringStatus.MONITORING
            monitorAdbDevices(
                adbPath = adbPath,
                onMessage = onMessage,
                scope = coroutineScope,
                onDeviceFound = onDeviceFound
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

    override suspend fun manageListeningStatus(
        monitorStatus: MonitorStatus,
        scope: CoroutineScope,
        onMessage: (InfoManagerData) -> Unit,
        onDeviceFound: (DeviceItem) -> Unit
    ) {
        if (_monitoringStatus.value == MonitoringStatus.MONITORING) {
            stopListening(
                coroutineScope = scope
            )
            onMessage(
                InfoManagerData(
                    message = ArgsText(textResId = Res.string.info_stop_listening)
                )
            )
        } else {
            startListening(
                onMessage = onMessage,
                coroutineScope = scope,
                onDeviceFound = onDeviceFound
            )
            onMessage(
                InfoManagerData(
                    message = ArgsText(textResId = Res.string.info_start_listening)
                )
            )
        }
    }

    override fun isMonitoring() = _monitoringStatus.value == MonitoringStatus.MONITORING

    private suspend fun monitorAdbDevices(
        adbPath: String,
        onMessage: (InfoManagerData) -> Unit,
        scope: CoroutineScope,
        onDeviceFound: (DeviceItem) -> Unit
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
                                val deviceExist = devices.value.any { it.deviceIdentifier == identifier }
                                val deviceAvailable = parts.last().trim() != ADB_DEVICE_OFFLINE

                                currentDevices.add(identifier)

                                if (!deviceExist && deviceAvailable) {
                                    addDevice(
                                        identifier = identifier,
                                        onMessage = onMessage,
                                        onDeviceFound = onDeviceFound
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
                            message = ArgsText(
                                textResId = Res.string.error_monitor_general,
                                formatArgs = listOf(exception.message ?: "")
                            ),
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
            val disconnectedIdentifiers = devices.value.map { it.deviceIdentifier } - currentDevices
            for (identifier in disconnectedIdentifiers) {
                removeDevice(
                    identifier = identifier,
                    onMessage = onMessage
                )
            }
        }
    }

    private suspend fun addDevice(
        identifier: String,
        onMessage: (InfoManagerData) -> Unit,
        onDeviceFound: (DeviceItem) -> Unit
    ) {
        getDeviceInfo(
            identifier = identifier
        ).also {
            _devices.value += it
            onDeviceFound(
                DeviceItem(
                    serialNumber = it.serialNumber,
                    model = it.model,
                    manufacturer = it.manufacturer,
                    brand = it.brand,
                )
            )
            onMessage(
                InfoManagerData(
                    message = ArgsText(
                        textResId = Res.string.info_add_device,
                        formatArgs = listOf(it.toString())
                    )
                )
            )
        }
    }

    private suspend fun updateDevice(
        identifier: String,
        onMessage: (InfoManagerData) -> Unit
    ) {
        getDeviceInfo(
            identifier = identifier
        ).also { deviceDetails ->
            _devices.value.indexOfFirst { it.deviceIdentifier == deviceDetails.deviceIdentifier }.also { index ->
                if (_devices.value[index] != deviceDetails) {
                    _devices.value = _devices.value.toMutableList().also { it[index] = deviceDetails }
                    onMessage(
                        InfoManagerData(
                            message = ArgsText(
                                textResId = Res.string.info_update_device,
                                formatArgs = listOf(deviceDetails.toString())
                            )
                        )
                    )
                }
            }
        }
    }

    private suspend fun getDeviceInfo(
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
            ?: getString(Res.string.info_not_connected),
        state = MonitoringStatus.MONITORING,
        privateSpaceIdentifier = getPrivateSpaceId(
            adbPath = adbPath,
            identifier = identifier
        )
    )

    private suspend fun removeDevice(
        identifier: String,
        onMessage: (InfoManagerData) -> Unit
    ) {
        val deviceToRemove = _devices.value.firstOrNull { it.deviceIdentifier == identifier }
        deviceToRemove?.let { device ->
            _devices.value = _devices.value.filter { it != device }
            onMessage(
                InfoManagerData(
                    message = ArgsText(
                        textResId = Res.string.info_remove_device,
                        formatArgs = listOf(device.toString())
                    ),
                    color = darkRed
                ),
            )
        }
    }

    private fun clearDevices() {
        _devices.value = emptyList()
    }
}

enum class MonitorStatus {
    START, STOP
}

