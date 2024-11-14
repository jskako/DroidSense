package ui.composable.sections.settings.ai

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import data.keys.AIKey
import data.repository.settings.SettingsSource
import notifications.InfoManagerData
import ui.composable.elements.SettingRow
import utils.getStringResource

@Composable
fun OpenApi(settingsSource: SettingsSource, onMessage: (InfoManagerData) -> Unit) {

    var openAPIKey by remember { mutableStateOf("") }

    Column {
        SettingRow(
            key = AIKey.OPEN_API,
            settingsSource = settingsSource,
            hintText = getStringResource("info.chat.gpt.hint"),
            saveTooltip = getStringResource("info.save.chat.gpt"),
            enableTooltip = getStringResource("info.enable.chat.gpt"),
            removeTooltip = getStringResource("info.disable.chat.gpt"),
            editMessage = getStringResource("info.message.edit.chat.gpt"),
            removeMessage = getStringResource("info.message.remove.chat.gpt"),
            onMessage = onMessage,
            onKeyFound = { openAPIKey = it },
        )
    }
}