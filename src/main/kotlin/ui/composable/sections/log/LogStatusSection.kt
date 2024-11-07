package ui.composable.sections.log

import adb.getDevicePropertyList
import adb.log.LogLevel
import adb.log.LogManager
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import data.model.items.LogItem
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import notifications.InfoManagerData
import ui.composable.elements.DropdownItem
import ui.composable.elements.OutlinedButton
import ui.composable.elements.window.DropdownTextItem
import utils.Colors.darkBlue
import utils.Colors.darkRed
import utils.Colors.transparentTextFieldDefault
import utils.DEVICE_PACKAGES
import utils.getStringResource
import java.util.UUID

@Composable
fun LogStatusSection(
    adbPath: String,
    identifier: String,
    serialNumber: String,
    logManager: LogManager,
    onMessage: (InfoManagerData) -> Unit,
    onLogLevelSelected: (LogLevel) -> Unit,
    searchText: String,
    onSearchTextChanged: (String) -> Unit,
    isRunning: Boolean,
    onIsRunning: (Boolean) -> Unit,
    onPackageSelected: (String) -> Unit,
    onUuidCreated: (UUID) -> Unit,
    onLastLog: (LogItem) -> Unit
) {
    val scope = rememberCoroutineScope()
    var selectedPackage by remember { mutableStateOf(getStringResource("info.log.starting.package")) }
    var selectedLogLevel by remember { mutableStateOf(LogLevel.VERBOSE) }

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
                    contentColor = when (isRunning) {
                        false -> darkBlue
                        true -> darkRed
                    },
                    onClick = {
                        scope.launch {
                            withContext(Default) {
                                when (isRunning) {
                                    false -> {
                                        logManager.startMonitoring(
                                            coroutineScope = this,
                                            packageName = selectedPackage,
                                            identifier = identifier,
                                            serialNumber = serialNumber,
                                            onMessage = onMessage,
                                            onLastLog = onLastLog,
                                            onUuidCreated = onUuidCreated
                                        )
                                    }

                                    true -> {
                                        logManager.stopMonitoring()
                                    }
                                }
                                onIsRunning(!isRunning)
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

                DropdownTextItem(
                    modifier = Modifier
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

                TextField(
                    value = searchText,
                    colors = transparentTextFieldDefault,
                    singleLine = true,
                    onValueChange = {
                        onSearchTextChanged(it)
                    },
                    placeholder = { Text(getStringResource("info.search")) },
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }
        }
    }
}