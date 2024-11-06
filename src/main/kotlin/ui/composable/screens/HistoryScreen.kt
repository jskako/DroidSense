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
import notifications.InfoManager
import ui.composable.elements.window.Sources
import ui.composable.sections.history.DevicesHistorySection
import ui.composable.sections.history.LogHistorySection
import ui.composable.sections.info.InfoSection

@Composable
fun HistoryScreen(
    sources: Sources
) {

    val scope = rememberCoroutineScope()
    val infoManager = remember { InfoManager() }
    var selectedOption by rememberSaveable { mutableStateOf(HistoryOption.LOGS) }

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
                            nameSource = sources.nameSource,
                            logHistorySource = sources.logHistorySource,
                            onMessage = {

                            }
                        )
                    }

                    HistoryOption.DEVICES -> {
                        DevicesHistorySection(
                            sources = sources,
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
