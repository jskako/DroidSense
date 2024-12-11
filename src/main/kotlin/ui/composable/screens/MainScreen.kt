package ui.composable.screens

import adb.DeviceManager
import adb.MonitorStatus
import adb.MonitoringStatus
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.jskako.droidsense.generated.resources.Res
import com.jskako.droidsense.generated.resources.info_usb_debugging_enabled
import com.jskako.droidsense.generated.resources.info_waiting_device
import data.ArgsText
import data.keys.SettingsKey
import kotlinx.coroutines.launch
import notifications.InfoManager
import notifications.InfoManagerData
import ui.application.WindowStateManager
import ui.composable.elements.CircularProgressBar
import ui.composable.elements.DividerColored
import ui.composable.elements.device.DeviceView
import ui.composable.elements.window.Sources
import ui.composable.sections.StatusSection
import ui.composable.sections.info.InfoSection
import utils.EMPTY_STRING
import utils.capitalizeFirstChar

@Composable
fun MainScreen(
    windowStateManager: WindowStateManager,
    sources: Sources
) {

    val adbPath by sources.settingsSource.get(SettingsKey.ADB.name).collectAsState(initial = "")
    val scrcpyPath by sources.settingsSource.get(SettingsKey.SCRCPY.name).collectAsState(initial = "")
    var searchText by remember { mutableStateOf(EMPTY_STRING) }
    val deviceSource by remember { mutableStateOf(sources.deviceSource) }
    val scope = rememberCoroutineScope()

    val deviceManager = remember(adbPath) {
        DeviceManager(
            adbPath = adbPath
        )
    }

    val infoManager = remember { InfoManager() }
    val devices by remember(deviceManager.devices.value) { mutableStateOf(deviceManager.devices.value) }

    val filteredDevices = devices.filter { device ->
        searchText.isEmpty()
                || device.serialNumber.contains(searchText, ignoreCase = true)
                || device.deviceIdentifier.contains(searchText, ignoreCase = true)
                || (device.manufacturer?.contains(searchText, ignoreCase = true) ?: false)
                || ("${device.manufacturer?.capitalizeFirstChar()} ${device.model}".contains(
            searchText,
            ignoreCase = true
        ))
    }

    LaunchedEffect(adbPath) {
        if (adbPath.isNotEmpty()) {
            deviceManager.manageListeningStatus(
                monitorStatus = MonitorStatus.START,
                scope = scope,
                onMessage = {
                    infoManager.showMessage(
                        infoManagerData = it,
                        scope = scope
                    )
                },
                onDeviceFound = { device ->
                    scope.launch {
                        if (deviceSource.by(device.serialNumber) == null) {
                            deviceSource.add(device)
                        }
                    }
                }
            )

            infoManager.showMessage(
                InfoManagerData(
                    message = ArgsText(
                        textResId = Res.string.info_usb_debugging_enabled,
                    ),
                    duration = null
                ),
                scope = scope
            )
        }
    }

    Column {
        InfoSection(
            onCloseClicked = { infoManager.clearInfoMessage() },
            message = infoManager.infoManagerData.value.message,
            color = infoManager.infoManagerData.value.color
        )
        StatusSection(
            scrCpyPath = scrcpyPath,
            deviceManager = deviceManager,
            windowStateManager = windowStateManager,
            sources = sources,
            onMessage = {
                infoManager.showMessage(
                    infoManagerData = it,
                    scope = scope
                )
            },
            devices = devices,
            searchText = searchText,
            onSearchTextChanged = {
                searchText = it
            },
            adbPath = adbPath
        )
        DividerColored()

        CircularProgressBar(
            text = ArgsText(textResId = Res.string.info_waiting_device),
            isVisible = filteredDevices.isEmpty()
                    && deviceManager.monitoringStatus.value == MonitoringStatus.MONITORING
        )

        if (adbPath.isNotEmpty() && filteredDevices.isNotEmpty()) {
            DeviceView(
                sources = sources,
                devices = filteredDevices,
                windowStateManager = windowStateManager,
                onMessage = {
                    infoManager.showMessage(
                        infoManagerData = it,
                        scope = scope
                    )
                },
                adbPath = adbPath,
                scrCpyPath = scrcpyPath
            )
        }
    }
}