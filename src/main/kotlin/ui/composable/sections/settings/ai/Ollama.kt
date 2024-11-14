package ui.composable.sections.settings.ai

import androidx.compose.runtime.Composable
import data.keys.AIKey
import data.repository.settings.SettingsSource
import notifications.InfoManagerData
import ui.composable.elements.SettingRow
import utils.getStringResource

@Composable
fun Ollama(settingsSource: SettingsSource, onMessage: (InfoManagerData) -> Unit) {
    SettingRow(
        key = AIKey.OLLAMA,
        settingsSource = settingsSource,
        hintText = getStringResource("info.ollama.url.hint"),
        saveTooltip = getStringResource("info.save.ollama.url"),
        enableTooltip = getStringResource("info.enable.ollama.url"),
        removeTooltip = getStringResource("info.disable.ollama.url"),
        editMessage = getStringResource("info.message.edit.ollama.url"),
        removeMessage = getStringResource("info.message.remove.ollama.url"),
        onMessage = onMessage
    )
}
