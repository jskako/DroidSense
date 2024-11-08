package ui.composable.screens

import adb.DeviceDetails
import adb.log.ExportOption
import adb.log.LogLevel
import adb.log.LogManager
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FolderOpen
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import data.model.items.NameItem
import data.model.items.PhoneItem
import kotlinx.coroutines.launch
import notifications.InfoManager
import notifications.InfoManagerData
import ui.composable.elements.CircularProgressBar
import ui.composable.elements.DividerColored
import ui.composable.elements.log.LogView
import ui.composable.elements.window.Sources
import ui.composable.sections.info.FunctionIconData
import ui.composable.sections.info.InfoSection
import ui.composable.sections.log.FontSize
import ui.composable.sections.log.LogSelectedButtonSection
import ui.composable.sections.log.LogStatusSection
import ui.composable.sections.log.MainButtonsSection
import utils.DATABASE_DATETIME
import utils.EMPTY_STRING
import utils.EXPORT_NAME_TIMESTAMP
import utils.LOG_MANAGER_NUMBER_OF_LINES
import utils.capitalizeFirstChar
import utils.getStringResource
import utils.getTimeStamp
import utils.openFolderAtPath
import java.util.UUID

@Composable
fun LogScreen(
    adbPath: String,
    logManager: LogManager,
    device: DeviceDetails,
    sources: Sources
) {

    var logLevel by remember { mutableStateOf(LogLevel.VERBOSE) }
    var searchText by remember { mutableStateOf(EMPTY_STRING) }
    var reverseLogs by remember { mutableStateOf(false) }
    var scrollToEnd by remember { mutableStateOf(true) }
    var saveToDatabase by remember { mutableStateOf(true) }
    var fontSize by remember { mutableStateOf(13.sp) }
    var isRunning by remember { mutableStateOf(false) }
    var exportInProgress by remember { mutableStateOf(false) }
    var selectedPackage by remember { mutableStateOf(EMPTY_STRING) }
    val logs by logManager.logs.collectAsState()
    val scope = rememberCoroutineScope()
    val selectedCount = logs.count { it.isSelected }
    val hasSelectedLogs = selectedCount > 0
    var selectionInProgress by remember { mutableStateOf(false) }
    val infoManager = remember { InfoManager() }
    var exportPath by remember { mutableStateOf<String?>(null) }
    var currentSessionUuid by remember { mutableStateOf(UUID(0, 0)) }

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

    suspend fun addName() {
        sources.nameSource.add(
            nameItem = NameItem(
                sessionUuid = currentSessionUuid,
                name = "${device.manufacturer?.capitalizeFirstChar()}_${device.model}_${device.serialNumber}_${
                    getTimeStamp(
                        EXPORT_NAME_TIMESTAMP
                    )
                }",
                dateTime = getTimeStamp(DATABASE_DATETIME),
                deviceSerialNumber = device.serialNumber
            )
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
            buttonVisible = infoManager.infoManagerData.value.buttonVisible
        )

        LogStatusSection(
            adbPath = adbPath,
            identifier = device.deviceIdentifier,
            logManager = logManager,
            serialNumber = device.serialNumber,
            onLogLevelSelected = {
                logLevel = it
            },
            searchText = searchText,
            onSearchTextChanged = {
                searchText = it
            },
            isRunning = isRunning,
            onIsRunning = {
                isRunning = it
            },
            onPackageSelected = {
                selectedPackage = it
            },
            onMessage = {
                showMessage(infoManagerData = it)
            },
            onSessionUuidCreated = { sessionUuid ->
                if (saveToDatabase) {
                    currentSessionUuid = sessionUuid
                    scope.launch {
                        addName()
                    }
                }
            },
            onLastLog = { log ->
                if (saveToDatabase) {
                    scope.launch {
                        if (sources.nameSource.by(currentSessionUuid) == null) {
                            addName()
                        }
                        sources.phoneSource.let {
                            if (it.by(serialNumber = device.serialNumber) == null) {
                                it.add(
                                    phoneItem = PhoneItem(
                                        serialNumber = device.serialNumber,
                                        model = device.model,
                                        manufacturer = device.manufacturer,
                                        brand = device.brand,
                                    )
                                )
                            }
                        }
                        sources.logHistorySource.add(log)
                    }
                }
            }
        )

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
                            logManager.clear(identifier = device.deviceIdentifier)
                            showMessage(getStringResource("info.logs.cleared"))
                        }
                    },
                    scrollToEnd = scrollToEnd,
                    onScrollToEnd = {
                        scrollToEnd = it
                    },
                    isRunning = isRunning,
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
                    isExportEnabled = (!exportInProgress && logs.isNotEmpty() && !isRunning),
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
                        isExportEnabled = (!exportInProgress && !isRunning),
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
                        filteredText = searchText,
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

