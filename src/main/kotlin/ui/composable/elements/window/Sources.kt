package ui.composable.elements.window

import data.repository.log.LogHistorySource
import data.repository.name.NameSource
import data.repository.phone.PhoneSource
import data.repository.settings.SettingsSource

data class Sources(
    val settingsSource: SettingsSource,
    val logHistorySource: LogHistorySource,
    val phoneSource: PhoneSource,
    val nameSource: NameSource
)
