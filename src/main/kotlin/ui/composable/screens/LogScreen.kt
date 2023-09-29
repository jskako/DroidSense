package ui.composable.screens

import adb.DeviceDetails
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.TabRowDefaults.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import log.LogManager
import ui.composable.elements.log.LogView
import ui.composable.sections.LazySection
import ui.composable.sections.LogStatusSection
import utils.LOG_MANAGER_NUMBER_OF_LINES

@Composable
fun LogScreen(device: DeviceDetails): LogManager {
    return LogManager().let { logManager ->
        Column {
            LogStatusSection(
                serialNumber = device.serialNumber,
                text = device.toString(),
                logManager = logManager
            )

            Divider(
                modifier = Modifier.fillMaxWidth(),
                color = Color.Gray,
                thickness = 1.dp
            )

            LazySection(view = { LogView(logManager.logs.takeLast(LOG_MANAGER_NUMBER_OF_LINES)) })
        }
        logManager
    }
}