package ui.composable.screens

import adb.DeviceDetails
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.TabRowDefaults.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import ui.composable.sections.LogStatusSection

@Composable
fun LogScreen(device: DeviceDetails) {
    Column {
        LogStatusSection(
            serialNumber = device.serialNumber,
            text = device.toString()
        )
        Divider(
            modifier = Modifier.fillMaxWidth(),
            color = Color.Gray,
            thickness = 1.dp
        )
    }
}