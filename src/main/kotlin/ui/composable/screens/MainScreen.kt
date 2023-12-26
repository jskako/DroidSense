package ui.composable.screens

import adb.DeviceManager
import adb.MonitorStatus
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.TabRowDefaults.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
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
    windowStateManager: WindowStateManager
) {

    val deviceManager = remember { DeviceManager() }
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
            deviceManager = deviceManager,
            windowStateManager = windowStateManager,
            onMessage = {
                infoManager.showMessage(
                    infoManagerData = it,
                    scope = scope
                )
            }
        )
        Divider(
            modifier = Modifier.fillMaxWidth(),
            color = Color.Gray,
            thickness = 1.dp
        )
        CircularProgressBar(
            text = getStringResource("info.waiting.device"),
            isVisible = deviceManager.devices.isEmpty()
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
                }
            )
        })
    }
}