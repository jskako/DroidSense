package ui.composable.screens

import adb.AdbManager.startListening
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import notifications.InfoManager.clearInfoMessage
import notifications.InfoManager.showInfoMessage
import ui.application.state.ProgressStateManager.progressVisible
import ui.composable.elements.LinearProgress
import ui.composable.sections.DeviceSection
import ui.composable.sections.InfoSection
import ui.composable.sections.LogSection
import utils.getStringResource

@Composable
fun MainScreen() {
    startListening(rememberCoroutineScope())
    showInfoMessage(getStringResource("info.usb.debugging.enabled"))

    Column {
        LinearProgress(
            isVisible = progressVisible.value
        )
        InfoSection(onCloseClicked = { clearInfoMessage() })
        DeviceSection()
        Divider(color = Color.Gray, thickness = 1.dp)
        LogSection()
    }
}