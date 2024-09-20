package ui.composable.screens

import adb.DeviceManager
import adb.MonitorStatus
import adb.MonitoringStatus
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import data.keys.SettingsKey
import data.repository.settings.SettingsSource
import notifications.InfoManager
import notifications.InfoManagerData
import ui.application.WindowStateManager
import ui.composable.elements.CircularProgressBar
import ui.composable.elements.device.DeviceView
import ui.composable.sections.InfoSection
import ui.composable.sections.LazySection
import ui.composable.sections.StatusSection
import utils.getStringResource

@Composable
fun MainScreen(
    windowStateManager: WindowStateManager,
    settingsSource: SettingsSource
) {

    val adbPath by remember { mutableStateOf(settingsSource.get(SettingsKey.ADB.name)) }
    val scrcpyPath by remember { mutableStateOf(settingsSource.get(SettingsKey.SCRCPY.name)) }

    val deviceManager = remember {
        DeviceManager(
            adbPath = adbPath
        )
    }
    val infoManager = remember { InfoManager() }
    val scope = rememberCoroutineScope()
    LaunchedEffect(Unit) {
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

    Column {
        InfoSection(
            onCloseClicked = { infoManager.clearInfoMessage() },
            message = infoManager.infoManagerData.value.message,
            color = infoManager.infoManagerData.value.color,
        )
        StatusSection(
            scrCpyPath = scrcpyPath,
            deviceManager = deviceManager,
            windowStateManager = windowStateManager,
            onMessage = {
                infoManager.showMessage(
                    infoManagerData = it,
                    scope = scope
                )
            }
        )
        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            thickness = 1.dp,
            color = Color.Gray
        )
        CircularProgressBar(
            text = getStringResource("info.waiting.device"),
            isVisible = deviceManager.devices.isEmpty() && deviceManager.monitoringStatus.value == MonitoringStatus.MONITORING
        )
        LazySection(view = {
            DeviceView(
                devices = deviceManager.devices,
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
        })
    }
}