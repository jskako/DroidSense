package ui.composable.sections

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
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
import notifications.InfoManagerData
import ui.composable.elements.DropdownItem
import ui.composable.elements.FilterText
import ui.composable.elements.HintBox
import ui.composable.elements.OutlinedButton
import ui.composable.elements.window.DropdownTextItem
import utils.Colors.darkBlue
import utils.Colors.darkRed
import utils.DEVICE_PACKAGES
import utils.EMPTY_STRING
import utils.getDevicePropertyList
import utils.getStringResource

@Composable
fun LogStatusSection(
    adbPath: String,
    identifier: String,
    logManager: LogManager,
    onMessage: (InfoManagerData) -> Unit,
    onLogLevelSelected: (LogLevel) -> Unit,
    onSearchTextChanged: (String) -> Unit,
    onIsRunning: (Boolean) -> Unit,
    onPackageSelected: (String) -> Unit
) {
    val scope = rememberCoroutineScope()
    var selectedPackage by remember { mutableStateOf(getStringResource("info.log.starting.package")) }
    var isRunning by remember { mutableStateOf(false) }
    var selectedLogLevel by remember { mutableStateOf(LogLevel.VERBOSE) }
    var searchText by remember { mutableStateOf(EMPTY_STRING) }
    var filterVisible by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .wrapContentWidth()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                OutlinedButton(
                    text = when (isRunning) {
                        false -> getStringResource("info.start.log.manager")
                        true -> getStringResource("info.stop.log.manager")
                    },
                    color = when (isRunning) {
                        false -> darkBlue
                        true -> darkRed
                    },
                    onClick = {
                        scope.launch {
                            withContext(Default) {
                                when (isRunning) {
                                    false -> {
                                        logManager.startMonitoringLogs(
                                            coroutineScope = this,
                                            packageName = selectedPackage,
                                            identifier = identifier,
                                            onMessage = onMessage
                                        )
                                    }

                                    true -> {
                                        logManager.stopMonitoringLogs()
                                    }
                                }
                                isRunning = !isRunning
                                onIsRunning(isRunning)
                            }
                        }
                    },
                    modifier = Modifier.wrapContentSize()
                )

                Spacer(modifier = Modifier.width(16.dp))

                DropdownItem(
                    list = getDevicePropertyList(
                        adbPath = adbPath,
                        identifier = identifier,
                        property = DEVICE_PACKAGES,
                        startingItem = getStringResource("info.log.starting.package")
                    ),
                    text = selectedPackage,
                    onItemSelected = { item ->
                        onPackageSelected(item)
                        selectedPackage = item
                    },
                    enabled = !isRunning,
                    buttonText = selectedPackage
                )

                Spacer(modifier = Modifier.width(16.dp))

                FilterText(
                    onClick = {
                        filterVisible = it
                    }
                )
            }

            if (filterVisible) {
                HintBox(
                    modifier = Modifier
                        .padding(top = 8.dp, bottom = 8.dp)
                        .fillMaxWidth(),
                    text = searchText,
                    onValueChanged = {
                        searchText = it
                        onSearchTextChanged(it)
                    }
                )

                DropdownTextItem(
                    modifier = Modifier
                        .padding(start = 8.dp, end = 8.dp)
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
            }
        }
    }
}