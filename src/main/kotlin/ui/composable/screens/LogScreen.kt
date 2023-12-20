package ui.composable.screens

import adb.DeviceDetails
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.TabRowDefaults.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import log.LogLevel
import log.LogManager
import ui.composable.elements.log.LogView
import ui.composable.sections.LazySection
import ui.composable.sections.LogStatusSection
import utils.LOG_MANAGER_NUMBER_OF_LINES

@Composable
fun LogScreen(device: DeviceDetails): LogManager {

    var logLevel by remember { mutableStateOf(LogLevel.VERBOSE) }
    var filteredText by remember { mutableStateOf("") }
    var reversedLogs by remember { mutableStateOf(false) }
    var scrollToEnd by remember { mutableStateOf(true) }

    return LogManager().let { logManager ->
        Column {
            LogStatusSection(
                serialNumber = device.serialNumber,
                text = device.toString(),
                logManager = logManager,
                onLogLevelSelected = {
                    logLevel = it
                },
                onSearchTextChanged = {
                    filteredText = it
                },
                onScrollToEnd = {
                    scrollToEnd = it
                },
                onReversedLogs = {
                    reversedLogs = it
                }
            )

            Divider(
                modifier = Modifier.fillMaxWidth(),
                color = Color.Gray,
                thickness = 1.dp
            )

            LazySection(
                modifier = Modifier.fillMaxWidth(),
                view = {
                    LogView(
                        logs = logManager.logs.takeLast(
                            LOG_MANAGER_NUMBER_OF_LINES
                        ),
                        logLevel = logLevel,
                        filteredText = filteredText,
                        reversedLogs = reversedLogs,
                        scrollToEnd = scrollToEnd
                    )
                }
            )
        }
        logManager
    }
}