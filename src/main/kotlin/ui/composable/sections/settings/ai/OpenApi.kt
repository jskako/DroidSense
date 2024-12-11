package ui.composable.sections.settings.ai

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.jskako.droidsense.generated.resources.Res
import com.jskako.droidsense.generated.resources.info_chat_gpt_hint
import com.jskako.droidsense.generated.resources.info_disable_chat_gpt
import com.jskako.droidsense.generated.resources.info_enable_chat_gpt
import com.jskako.droidsense.generated.resources.info_message_edit_chat_gpt
import com.jskako.droidsense.generated.resources.info_message_remove_chat_gpt
import com.jskako.droidsense.generated.resources.info_save_chat_gpt
import data.keys.AIKey
import data.repository.settings.SettingsSource
import notifications.InfoManagerData
import ui.composable.elements.SettingRow

@Composable
fun OpenApi(settingsSource: SettingsSource, onMessage: (InfoManagerData) -> Unit) {

    var openAPIKey by remember { mutableStateOf("") }

    Column {
        SettingRow(
            key = AIKey.OPEN_API,
            settingsSource = settingsSource,
            hintText = Res.string.info_chat_gpt_hint,
            saveTooltip = Res.string.info_save_chat_gpt,
            enableTooltip = Res.string.info_enable_chat_gpt,
            removeTooltip = Res.string.info_disable_chat_gpt,
            editMessage = Res.string.info_message_edit_chat_gpt,
            removeMessage = Res.string.info_message_remove_chat_gpt,
            onMessage = onMessage,
            onKeyFound = { openAPIKey = it },
        )
    }
}