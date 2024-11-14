package ui.composable.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Dataset
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Scanner
import androidx.compose.material.icons.filled.Settings
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
import ui.composable.sections.info.InfoSection
import ui.composable.sections.settings.DatabaseSection
import ui.composable.sections.settings.GeneralSection
import ui.composable.sections.settings.InfoSection
import ui.composable.sections.settings.ai.AISection

@Composable
fun SettingsScreen(
    sources: Sources
) {

    val scope = rememberCoroutineScope()
    val infoManager = remember { InfoManager() }
    var selectedOption by rememberSaveable { mutableStateOf(SettingsOption.GENERAL) }

    Column {
        InfoSection(
            onCloseClicked = { infoManager.clearInfoMessage() },
            message = infoManager.infoManagerData.value.message,
            color = infoManager.infoManagerData.value.color
        )

        NavigationSuiteScaffold(
            navigationSuiteItems = {
                SettingsOption.entries.forEach { entry ->
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
                    SettingsOption.GENERAL -> GeneralSection(
                        settingsSource = sources.settingsSource,
                        onMessage = {
                            infoManager.showMessage(
                                infoManagerData = it,
                                scope = scope
                            )
                        }
                    )

                    SettingsOption.DATABASE -> DatabaseSection(
                        sources = sources,
                        onMessage = {
                            infoManager.showMessage(
                                infoManagerData = it,
                                scope = scope
                            )
                        }
                    )

                    SettingsOption.AI -> AISection(
                        settingsSource = sources.settingsSource,
                        onMessage = {
                            infoManager.showMessage(
                                infoManagerData = it,
                                scope = scope
                            )
                        }
                    )

                    SettingsOption.INFO -> InfoSection()
                }
            }
        )
    }
}

enum class SettingsOption {
    GENERAL {
        override fun title() = "General"
        override fun icon() = Icons.Default.Settings
    },

    DATABASE {
        override fun title() = "Database"
        override fun icon() = Icons.Default.Dataset
    },

    AI {
        override fun title() = "AI"
        override fun icon() = Icons.Default.Scanner
    },

    INFO {
        override fun title() = "Info"
        override fun icon() = Icons.Default.Info
    };

    abstract fun title(): String
    abstract fun icon(): ImageVector
}
