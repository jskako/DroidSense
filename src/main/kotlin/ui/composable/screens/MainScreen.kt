package ui.composable.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import ui.application.state.ProgressStateManager.progressVisible
import ui.composable.elements.LinearProgress
import ui.composable.sections.DeviceSection
import ui.composable.sections.InfoSection
import ui.composable.sections.LogSection

@Composable
fun MainScreen() {
    Column {
        LinearProgress(
            isVisible = progressVisible.value
        )
        InfoSection()
        DeviceSection()
        Divider(color = Color.Gray, thickness = 1.dp)
        LogSection()
    }
}