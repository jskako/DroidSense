package ui.composable.elements.window

import data.repository.log.LogHistorySource
import data.repository.settings.SettingsSource

data class Sources(
    val settingsSource: SettingsSource,
    val logHistorySource: LogHistorySource
)
