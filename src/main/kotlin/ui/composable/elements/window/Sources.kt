package ui.composable.elements.window

import data.repository.ai.ollama.model.OllamaModelSource
import data.repository.ai.ollama.url.OllamaUrlSource
import data.repository.device.DeviceSource
import data.repository.log.LogHistorySource
import data.repository.name.NameSource
import data.repository.settings.SettingsSource

data class Sources(
    val settingsSource: SettingsSource,
    val logHistorySource: LogHistorySource,
    val deviceSource: DeviceSource,
    val nameSource: NameSource,
    val ollamaModelSource: OllamaModelSource,
    val ollamaUrlSource: OllamaUrlSource
)
