package ui.composable.sections

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import log.LogLevel
import log.LogManager
import ui.composable.elements.BasicTextCaption
import ui.composable.elements.DropdownItem
import ui.composable.elements.FilterText
import ui.composable.elements.HintText
import ui.composable.elements.OutlinedButton
import ui.composable.elements.StyledTextCaption
import ui.composable.elements.window.DropdownTextItem
import utils.DEVICE_PACKAGES
import utils.getDevicePropertyList
import utils.getStringResource

@Composable
fun LogStatusSection(
    serialNumber: String,
    text: String,
    logManager: LogManager,
    onLogLevelSelected: (LogLevel) -> Unit,
    searchText: String,
    onSearchTextChanged: (String) -> Unit,
) {
    val scope = rememberCoroutineScope()
    var selectedItem by remember { mutableStateOf(getStringResource("info.log.starting.package")) }
    var operation by remember { mutableStateOf(LogOperation.START) }
    var selectedLogLevel by remember { mutableStateOf(LogLevel.VERBOSE) }
    var filterVisible by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .wrapContentWidth()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column {
            StyledTextCaption(
                text1 = text,
                text2 = "($serialNumber)",
                specialChar = ""
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                OutlinedButton(
                    text = when (operation) {
                        LogOperation.START -> getStringResource("info.start.log.manager")
                        LogOperation.STOP -> getStringResource("info.stop.log.manager")
                    },
                    onClick = {
                        scope.launch {
                            withContext(Default) {
                                operation = when (operation) {
                                    LogOperation.START -> {
                                        logManager.startMonitoringLogs(
                                            coroutineScope = this,
                                            packageName = selectedItem,
                                            serialNumber = serialNumber
                                        )
                                        LogOperation.STOP
                                    }

                                    LogOperation.STOP -> {
                                        logManager.stopMonitoringLogs()
                                        LogOperation.START
                                    }
                                }
                            }
                        }
                    },
                    modifier = Modifier.wrapContentSize()
                )

                Spacer(modifier = Modifier.width(16.dp))

                DropdownItem(
                    list = getDevicePropertyList(
                        serialNumber = serialNumber,
                        property = DEVICE_PACKAGES,
                        startingItem = getStringResource("info.log.starting.package")
                    ),
                    text = selectedItem,
                    onItemSelected = { item ->
                        selectedItem = item
                    },
                    enabled = operation == LogOperation.START,
                    buttonText = selectedItem
                )

                Spacer(modifier = Modifier.width(16.dp))

                FilterText(
                    onClick = {
                        filterVisible = it
                    }
                )
            }

            if (filterVisible) {
                HintText(
                    modifier = Modifier
                        .padding(top = 8.dp, bottom = 8.dp)
                        .fillMaxWidth(),
                    text = searchText,
                    onValueChanged = {
                        onSearchTextChanged(it)
                    }
                )

                Row {
                    DropdownTextItem(
                        modifier = Modifier
                            .padding(start = 16.dp, end = 16.dp)
                            .fillMaxWidth(),
                        list = enumValues<LogLevel>().map { it.name },
                        text = selectedLogLevel.name,
                        onItemSelected = { item ->
                            LogLevel.valueOf(item).also {
                                selectedLogLevel = it
                                onLogLevelSelected(it)
                            }
                        }
                    )

                    // TODO - Fix this
                    /*CheckboxBoxText(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, end = 16.dp),
                        icon = Icons.Default.List,
                        text = "Reversed logs",
                        checkedState = reversedLogs,
                        onChecked = {
                            reversedLogs = it
                            scrollToEnd = when (it) {
                                true -> {
                                    false
                                }

                                false -> {
                                    true
                                }
                            }
                        }
                    )

                    if (!reversedLogs) {
                        CheckboxBoxText(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 16.dp, end = 16.dp),
                            icon = Icons.Default.ArtTrack,
                            text = "Scroll to End",
                            checkedState = scrollToEnd,
                            onChecked = {
                                scrollToEnd = it
                            }
                        )
                    }*/
                }
            }
        }
    }
}

private enum class LogOperation {
    START, STOP
}