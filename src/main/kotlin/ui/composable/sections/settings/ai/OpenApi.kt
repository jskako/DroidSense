package ui.composable.sections.settings.ai

import androidx.compose.runtime.Composable
import data.keys.AIKey
import data.repository.settings.SettingsSource
import notifications.InfoManagerData
import ui.composable.elements.SettingRow
import utils.getStringResource

@Composable
fun OpenApi(settingsSource: SettingsSource, onMessage: (InfoManagerData) -> Unit) {
    SettingRow(
        key = AIKey.OPEN_API,
        settingsSource = settingsSource,
        hintText = getStringResource("info.chat.gpt.hint"),
        saveTooltip = getStringResource("info.save.chat.gpt"),
        enableTooltip = getStringResource("info.enable.chat.gpt"),
        removeTooltip = getStringResource("info.disable.chat.gpt"),
        editMessage = getStringResource("info.message.edit.chat.gpt"),
        removeMessage = getStringResource("info.message.remove.chat.gpt"),
        onMessage = onMessage
    )
}