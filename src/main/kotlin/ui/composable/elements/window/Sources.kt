package ui.composable.elements.window

import data.repository.ai.AIHistorySource
import data.repository.ai.model.ModelSource
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
    val modelSource: ModelSource,
    val ollamaUrlSource: OllamaUrlSource,
    val aiHistorySource: AIHistorySource
)
