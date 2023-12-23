package ui.composable.screens

import adb.DeviceDetails
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowOutward
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MoveDown
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.TextDecrease
import androidx.compose.material.icons.filled.TextIncrease
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import log.LogLevel
import log.LogManager
import ui.composable.elements.CircularProgressBar
import ui.composable.elements.DividerColored
import ui.composable.elements.iconButtons.IconButtonsColumn
import ui.composable.elements.iconButtons.IconButtonsData
import ui.composable.elements.log.LogView
import ui.composable.sections.LazySection
import ui.composable.sections.LogStatusSection
import utils.Colors.darkBlue
import utils.Colors.darkRed
import utils.LOG_MANAGER_NUMBER_OF_LINES
import utils.getStringResource

@Composable
fun LogScreen(
    logManager: LogManager,
    device: DeviceDetails
) {

    var logLevel by remember { mutableStateOf(LogLevel.VERBOSE) }
    var filteredText by remember { mutableStateOf("") }
    var reverseLogs by remember { mutableStateOf(false) }
    var scrollToEnd by remember { mutableStateOf(true) }
    var saveToDatabase by remember { mutableStateOf(true) }
    var fontSize by remember { mutableStateOf(13.sp) }
    var isRunning by remember { mutableStateOf(false) }
    var selectedPackage by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    Column {
        LogStatusSection(
            serialNumber = device.serialNumber,
            logManager = logManager,
            onLogLevelSelected = {
                logLevel = it
            },
            onSearchTextChanged = {
                filteredText = it
            },
            onIsRunning = {
                isRunning = it
            },
            onPackageSelected = {
                selectedPackage = it
            }
        )

        DividerColored()

        Row {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(2.dp)
            ) {
                IconButtonsColumn(
                    listOf(
                        IconButtonsData(
                            icon = Icons.Default.Delete,
                            contentDescription = getStringResource("info.clear.logs"),
                            function = {
                                scope.launch {
                                    logManager.clearLogs()
                                }
                            }
                        ),
                        IconButtonsData(
                            modifier = Modifier.background(
                                color = if (scrollToEnd) darkBlue else Color.Transparent,
                                shape = CircleShape
                            ),
                            icon = Icons.Default.MoveDown,
                            contentDescription = getStringResource("info.scroll.end"),
                            tint = if (scrollToEnd) Color.White else darkBlue,
                            function = { scrollToEnd = !scrollToEnd }
                        ),
                        IconButtonsData(
                            modifier = Modifier.background(
                                color = if (reverseLogs) darkBlue else Color.Transparent,
                                shape = CircleShape
                            ),
                            icon = Icons.Default.ArrowOutward,
                            contentDescription = getStringResource("info.reversed"),
                            tint = if (reverseLogs) Color.White else darkBlue,
                            function = { reverseLogs = !reverseLogs }
                        ),
                        IconButtonsData(
                            icon = Icons.Default.TextIncrease,
                            contentDescription = getStringResource("info.font.size.increase"),
                            function = { fontSize *= 1.1f }
                        ),
                        IconButtonsData(
                            icon = Icons.Default.TextDecrease,
                            contentDescription = getStringResource("info.font.size.decrease"),
                            function = { fontSize /= 1.1f }
                        ),
                        IconButtonsData(
                            modifier = Modifier.background(
                                color = if (saveToDatabase) darkRed else Color.Transparent,
                                shape = CircleShape
                            ),
                            icon = Icons.Default.Save,
                            contentDescription = getStringResource("info.save.database"),
                            tint = if (saveToDatabase) Color.White else darkRed,
                            function = { saveToDatabase = !saveToDatabase }
                        ),
                    )
                )
            }
            if (logManager.logs.isEmpty() && isRunning) {
                CircularProgressBar(
                    text = "${getStringResource("info.waiting.application.logs")}\n$selectedPackage",
                    isVisible = true
                )
            } else {
                LazySection(
                    modifier = Modifier.fillMaxWidth(),
                    view = {
                        LogView(
                            logs = logManager.logs.takeLast(
                                LOG_MANAGER_NUMBER_OF_LINES
                            ),
                            logLevel = logLevel,
                            filteredText = filteredText,
                            reversedLogs = reverseLogs,
                            scrollToEnd = scrollToEnd,
                            fontSize = fontSize
                        )
                    }
                )
            }
        }
    }
}