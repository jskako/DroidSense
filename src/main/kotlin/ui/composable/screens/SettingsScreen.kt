package ui.composable.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Copyright
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.useResource
import com.mikepenz.aboutlibraries.ui.compose.m3.LibrariesContainer
import data.repository.settings.SettingsSource
import notifications.InfoManager
import ui.composable.sections.info.InfoSection
import ui.composable.sections.settings.AboutSection
import ui.composable.sections.settings.CopyrightSection
import ui.composable.sections.settings.GeneralSection
import utils.ABOUT_LIBRARIES_JSON_NAME

@Composable
fun SettingsScreen(
    settingsSource: SettingsSource
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
                        settingsSource = settingsSource,
                        onMessage = {
                            infoManager.showMessage(
                                infoManagerData = it,
                                scope = scope
                            )
                        }
                    )

                    SettingsOption.DATABASE -> {}
                    SettingsOption.AI -> {}
                    SettingsOption.ABOUT -> AboutSection()
                    SettingsOption.LICENSES -> {
                        LibrariesContainer(
                            useResource(ABOUT_LIBRARIES_JSON_NAME) {
                                it.bufferedReader().readText()
                            }, Modifier.fillMaxSize()
                        )
                    }

                    SettingsOption.COPYRIGHT -> CopyrightSection()
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

    ABOUT {
        override fun title() = "About"
        override fun icon() = Icons.Default.Info
    },

    LICENSES {
        override fun title() = "Licenses"
        override fun icon() = Icons.AutoMirrored.Filled.List
    },

    COPYRIGHT {
        override fun title() = "Copyright"
        override fun icon() = Icons.Default.Copyright
    },;

    abstract fun title(): String
    abstract fun icon(): ImageVector
}
