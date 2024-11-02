package ui.composable.screens

import adb.DeviceManager
import adb.MonitorStatus
import adb.MonitoringStatus
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import data.keys.SettingsKey
import notifications.InfoManager
import notifications.InfoManagerData
import ui.application.WindowStateManager
import ui.composable.elements.CircularProgressBar
import ui.composable.elements.device.DeviceView
import ui.composable.elements.window.Sources
import ui.composable.sections.StatusSection
import ui.composable.sections.info.InfoSection
import utils.EMPTY_STRING
import utils.capitalizeFirstChar
import utils.getStringResource

@Composable
fun MainScreen(
    windowStateManager: WindowStateManager,
    sources: Sources
) {

    val adbPath by sources.settingsSource.get(SettingsKey.ADB.name).collectAsState(initial = "")
    val scrcpyPath by sources.settingsSource.get(SettingsKey.SCRCPY.name).collectAsState(initial = "")
    var searchText by remember { mutableStateOf(EMPTY_STRING) }

    val deviceManager = remember(adbPath) {
        DeviceManager(
            adbPath = adbPath
        )
    }

    val infoManager = remember { InfoManager() }
    val scope = rememberCoroutineScope()
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
                }
            )

            infoManager.showMessage(
                InfoManagerData(
                    message = getStringResource("info.usb.debugging.enabled"),
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
        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            thickness = 1.dp,
            color = Color.Gray
        )
        CircularProgressBar(
            text = getStringResource("info.waiting.device"),
            isVisible = filteredDevices.isEmpty()
                    && deviceManager.monitoringStatus.value == MonitoringStatus.MONITORING
        )

        if (adbPath.isNotEmpty() && filteredDevices.isNotEmpty()) {
            DeviceView(
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