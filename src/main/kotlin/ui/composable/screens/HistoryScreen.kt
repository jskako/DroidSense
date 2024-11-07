package ui.composable.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Devices
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.vector.ImageVector
import data.model.items.PhoneItem.Companion.emptyPhoneItem
import notifications.InfoManager
import ui.application.WindowStateManager
import ui.composable.elements.window.Sources
import ui.composable.sections.history.DevicesHistorySection
import ui.composable.sections.history.LogHistorySection
import ui.composable.sections.info.InfoSection

@Composable
fun HistoryScreen(
    windowStateManager: WindowStateManager,
    sources: Sources
) {

    val scope = rememberCoroutineScope()
    val infoManager = remember { InfoManager() }
    var selectedOption by rememberSaveable { mutableStateOf(HistoryOption.LOGS) }
    var phoneItem by remember { mutableStateOf(emptyPhoneItem) }

    Column {
        InfoSection(
            onCloseClicked = { infoManager.clearInfoMessage() },
            message = infoManager.infoManagerData.value.message,
            color = infoManager.infoManagerData.value.color
        )

        NavigationSuiteScaffold(
            navigationSuiteItems = {
                HistoryOption.entries.forEach { entry ->
                    item(
                        icon = {
                            Icon(
                                entry.icon(),
                                contentDescription = entry.title()
                            )
                        },
                        label = { Text(entry.title()) },
                        selected = entry == selectedOption,
                        onClick = {
                            selectedOption = entry
                        }
                    )
                }
            },
            content = {
                when (selectedOption) {
                    HistoryOption.LOGS -> {
                        LogHistorySection(
                            windowStateManager = windowStateManager,
                            nameSource = sources.nameSource,
                            logHistorySource = sources.logHistorySource,
                            phoneItem = phoneItem,
                            onFilterClear = {
                                phoneItem = emptyPhoneItem
                            },
                            onMessage = { infoManagerData ->
                                infoManager.showMessage(
                                    infoManagerData = infoManagerData,
                                    scope = scope
                                )
                            }
                        )
                    }

                    HistoryOption.DEVICES -> {
                        DevicesHistorySection(
                            sources = sources,
                            onPhoneItemClicked = {
                                phoneItem = it
                                selectedOption = HistoryOption.LOGS

                            },
                            onMessage = {

                            }
                        )
                    }
                }
            }
        )
    }
}

enum class HistoryOption {
    LOGS {
        override fun title() = "Logs"
        override fun icon() = Icons.AutoMirrored.Filled.List
    },

    DEVICES {
        override fun title() = "Devices"
        override fun icon() = Icons.Default.Devices
    };

    abstract fun title(): String
    abstract fun icon(): ImageVector
}
