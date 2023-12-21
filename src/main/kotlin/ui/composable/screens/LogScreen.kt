package ui.composable.screens

import adb.DeviceDetails
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowOutward
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MoveDown
import androidx.compose.material.icons.filled.TextDecrease
import androidx.compose.material.icons.filled.TextIncrease
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import log.LogLevel
import log.LogManager
import ui.composable.elements.DividerColored
import ui.composable.elements.log.LogView
import ui.composable.sections.LazySection
import ui.composable.sections.LogStatusSection
import utils.Colors.darkBlue
import utils.LOG_MANAGER_NUMBER_OF_LINES
import utils.getStringResource

@Composable
fun LogScreen(device: DeviceDetails): LogManager {

    var logLevel by remember { mutableStateOf(LogLevel.VERBOSE) }
    var filteredText by remember { mutableStateOf("") }
    var reversedLogs by remember { mutableStateOf(false) }
    var scrollToEnd by remember { mutableStateOf(true) }
    var fontSize by remember { mutableStateOf(12.sp) }

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
                }
            )

            DividerColored()

            Row {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(2.dp)
                ) {
                    IconButton(
                        onClick = {
                            // TODO - delete logs
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = getStringResource("info.scroll.end"),
                            tint = darkBlue
                        )
                    }

                    IconButton(
                        onClick = {
                            scrollToEnd = !scrollToEnd
                        },
                        modifier = Modifier.background(
                            color = if (scrollToEnd) darkBlue else Color.Transparent,
                            shape = CircleShape
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.MoveDown,
                            contentDescription = getStringResource("info.scroll.end"),
                            tint = if (scrollToEnd) Color.White else darkBlue
                        )
                    }

                    IconButton(
                        onClick = {
                            reversedLogs = !reversedLogs
                        },
                        modifier = Modifier.background(
                            color = if (reversedLogs) darkBlue else Color.Transparent,
                            shape = CircleShape
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowOutward,
                            contentDescription = getStringResource("info.reversed"),
                            tint = if (reversedLogs) Color.White else darkBlue
                        )
                    }

                    IconButton(
                        onClick = {
                            fontSize *= 1.1f
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.TextIncrease,
                            contentDescription = getStringResource("info.font.size.increase"),
                            tint = darkBlue
                        )
                    }

                    IconButton(
                        onClick = {
                            fontSize /= 1.1f
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.TextDecrease,
                            contentDescription = getStringResource("info.font.size.decrease"),
                            tint = darkBlue
                        )
                    }
                }

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
                            scrollToEnd = scrollToEnd,
                            fontSize = fontSize
                        )
                    }
                )
            }
        }
        logManager
    }
}