package ui.composable.screens

import adb.DeviceDetails
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FolderOpen
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import log.ExportOption
import log.LogLevel
import log.LogManager
import notifications.InfoManager
import notifications.InfoManagerData
import ui.composable.elements.CircularProgressBar
import ui.composable.elements.DividerColored
import ui.composable.elements.log.LogView
import ui.composable.sections.info.FunctionIconData
import ui.composable.sections.info.InfoSection
import ui.composable.sections.log.FontSize
import ui.composable.sections.log.LogSelectedButtonSection
import ui.composable.sections.log.LogStatusSection
import ui.composable.sections.log.MainButtonsSection
import utils.EMPTY_STRING
import utils.LOG_MANAGER_NUMBER_OF_LINES
import utils.getStringResource
import utils.openFolderAtPath

@Composable
fun LogScreen(
    adbPath: String,
    logManager: LogManager,
    device: DeviceDetails
) {

    var logLevel by remember { mutableStateOf(LogLevel.VERBOSE) }
    var filteredText by remember { mutableStateOf(EMPTY_STRING) }
    var reverseLogs by remember { mutableStateOf(false) }
    var scrollToEnd by remember { mutableStateOf(true) }
    var saveToDatabase by remember { mutableStateOf(true) }
    var fontSize by remember { mutableStateOf(13.sp) }
    var isRunning by remember { mutableStateOf(false) }
    var exportInProgress by remember { mutableStateOf(false) }
    var selectedPackage by remember { mutableStateOf(EMPTY_STRING) }
    val logs by remember { mutableStateOf(logManager.logs) }
    val scope = rememberCoroutineScope()
    val selectedCount = logs.count { it.isSelected }
    val hasSelectedLogs = selectedCount > 0
    var selectionInProgress by remember { mutableStateOf(false) }
    val infoManager = remember { InfoManager() }
    var exportPath by remember { mutableStateOf<String?>(null) }

    fun showMessage(message: String) {
        infoManager.showMessage(
            infoManagerData = InfoManagerData(
                message = message
            ),
            scope = scope
        )
    }

    fun showMessage(infoManagerData: InfoManagerData) {
        infoManager.showMessage(
            infoManagerData = infoManagerData,
            scope = scope
        )
    }

    Column {

        InfoSection(
            onCloseClicked = {
                infoManager.clearInfoMessage()
            },
            message = infoManager.infoManagerData.value.message,
            color = infoManager.infoManagerData.value.color,
            onExtraClicked = exportPath?.let {
                FunctionIconData(
                    function = {
                        showMessage(infoManagerData = openFolderAtPath(path = it))
                    },
                    icon = Icons.Default.FolderOpen
                )
            },
            onDone = {
                exportPath = null
            }
        )

        LogStatusSection(
            adbPath = adbPath,
            identifier = device.deviceIdentifier,
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
            },
            onMessage = {
                showMessage(infoManagerData = it)
            })

        DividerColored()

        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .weight(0.1f)
            ) {
                MainButtonsSection(
                    onClearLogs = {
                        scope.launch {
                            logManager.clear()
                            showMessage(getStringResource("info.logs.cleared"))
                        }
                    },
                    scrollToEnd = scrollToEnd,
                    onScrollToEnd = {
                        scrollToEnd = it
                    },
                    reverseLogs = reverseLogs,
                    onReverseLogs = {
                        scrollToEnd = false
                        reverseLogs = it
                    },
                    onFontSize = {
                        when (it) {
                            FontSize.INCREASE -> fontSize *= 1.1f
                            FontSize.DECREASE -> fontSize /= 1.1f
                        }
                    },
                    saveToDatabase = saveToDatabase,
                    onSaveToDatabase = {
                        saveToDatabase = it
                    },
                    onExportLogs = {
                        exportInProgress = true
                        scope.launch {
                            logManager.export(
                                onExportDone = { exportData ->
                                    exportInProgress = false
                                    exportPath = exportData.path
                                    showMessage(infoManagerData = exportData.infoManagerData)
                                }
                            )
                        }
                    },
                    isExportEnabled = (!exportInProgress && logs.isNotEmpty()),
                    isSelectEnabled = !isRunning && !selectionInProgress && logs.isNotEmpty(),
                    onSelect = {
                        selectionInProgress = true
                        scope.launch {
                            logManager.isSelected(
                                selectOption = it,
                                onSelectDone = {
                                    selectionInProgress = false
                                }
                            )
                        }
                    },
                    isClearLogsEnabled = logs.isNotEmpty(),
                    scrollToEndEnabled = !reverseLogs
                )

                if (hasSelectedLogs) {
                    HorizontalDivider(modifier = Modifier.padding(8.dp))

                    LogSelectedButtonSection(
                        onExportLogs = {
                            exportInProgress = true
                            scope.launch {
                                logManager.export(
                                    exportOption = ExportOption.SELECTED,
                                    onExportDone = { exportData ->
                                        exportInProgress = false
                                        exportPath = exportData.path
                                        showMessage(infoManagerData = exportData.infoManagerData)
                                    }
                                )
                            }
                        },
                        onCopyLogs = {
                            scope.launch {
                                logManager.copy(
                                    exportOption = ExportOption.SELECTED
                                )
                            }
                        },
                        isExportEnabled = true,
                        selectedLogsSize = selectedCount,
                        onInfoMessage = {
                            showMessage(infoManagerData = it)
                        }
                    )
                }
            }

            Column(
                modifier = Modifier
                    .weight(0.9f)
            ) {
                if (logs.isEmpty() && isRunning) {
                    CircularProgressBar(
                        text = buildString {
                            appendLine(getStringResource("info.waiting.application.logs"))
                            appendLine(selectedPackage)
                        },
                        isVisible = true
                    )
                } else {
                    LogView(
                        logs = logs.takeLast(
                            LOG_MANAGER_NUMBER_OF_LINES
                        ),
                        logLevel = logLevel,
                        filteredText = filteredText,
                        reversedLogs = reverseLogs,
                        scrollToEnd = scrollToEnd,
                        fontSize = fontSize,
                        onLogSelected = { uuid ->
                            if (!selectionInProgress) {
                                logManager.isSelected(uuid = uuid)
                            }
                        }
                    )
                }
            }
        }
    }
}

