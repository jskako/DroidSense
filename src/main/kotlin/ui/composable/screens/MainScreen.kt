package ui.composable.screens

import adb.AdbDeviceManager.startListening
import adb.DeviceManager
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
import notifications.InfoManager.clearInfoMessage
import notifications.InfoManager.showInfoMessage
import ui.composable.elements.CircularProgressBar
import ui.composable.elements.device.DeviceView
import ui.composable.sections.InfoSection
import ui.composable.sections.LazySection
import ui.composable.sections.StatusSection
import utils.getStringResource

@Composable
fun MainScreen() {

    val deviceManager = remember { DeviceManager() }
    val scope = rememberCoroutineScope()
    LaunchedEffect(Unit) {
        startListening(
            coroutineScope = scope,
            deviceManager = deviceManager
        )
    }
    showInfoMessage(getStringResource("info.usb.debugging.enabled"))

    Column {
        InfoSection(
            onCloseClicked = { clearInfoMessage() }
        )
        StatusSection(deviceManager = deviceManager)
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
                devices = deviceManager.devices
            )
        })
    }
}