package ui.composable.sections

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import log.LogManager
import ui.composable.BasicTextCaption
import ui.composable.elements.DropdownItem
import ui.composable.elements.OutlinedButton
import utils.DEVICE_PACKAGES
import utils.getDevicePropertyList
import utils.getStringResource

@Composable
fun LogStatusSection(
    serialNumber: String,
    text: String,
    logManager: LogManager
) {
    val scope = rememberCoroutineScope()
    var selectedItem by remember { mutableStateOf(getStringResource("info.log.starting.package")) }
    var operation by remember { mutableStateOf(LogOperation.START) }

    Box(
        modifier = Modifier
            .wrapContentWidth()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column {
            BasicTextCaption(
                text1 = text,
                text2 = "($serialNumber)",
                specialChar = ""
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row {
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
                    onItemSelected = { item ->
                        selectedItem = item
                    },
                    visible = operation == LogOperation.START
                )
            }
        }
    }
}

private enum class LogOperation {
    START, STOP
}