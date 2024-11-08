package ui.composable.sections.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Save
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import data.keys.AIKey
import data.repository.settings.SettingsSource
import kotlinx.coroutines.launch
import notifications.InfoManagerData
import ui.composable.elements.OutlinedText
import ui.composable.elements.iconButtons.TooltipIconButton
import utils.Colors.darkBlue
import utils.Colors.darkRed
import utils.Colors.lightGray
import utils.getStringResource

@Composable
fun AISection(
    settingsSource: SettingsSource,
    onMessage: (InfoManagerData) -> Unit
) {

    val scope = rememberCoroutineScope()

    var chatGPTAPIkey by remember { mutableStateOf("") }
    val chatGPTAPIkeyDatabase by settingsSource.get(AIKey.CHATGPT.name).collectAsState(initial = "")
    val isChatGptButtonEnabled =
        chatGPTAPIkey.trim().isNotEmpty() && chatGPTAPIkey.trim() != chatGPTAPIkeyDatabase.trim()

    LaunchedEffect(chatGPTAPIkeyDatabase) {
        chatGPTAPIkey = chatGPTAPIkeyDatabase
    }

    Column(
        modifier = Modifier
            .padding(vertical = 16.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedText(
                modifier = Modifier
                    .weight(1f),
                text = chatGPTAPIkey,
                hintText = getStringResource("info.chat.gpt.hint"),
                onValueChanged = { changedPath ->
                    chatGPTAPIkey = changedPath
                }
            )

            TooltipIconButton(
                modifier = Modifier.then(
                    if (chatGPTAPIkeyDatabase.isNotEmpty()) Modifier else Modifier.padding(end = 8.dp),
                ),
                isEnabled = isChatGptButtonEnabled,
                tint = if (isChatGptButtonEnabled) darkBlue else lightGray,
                icon = if (chatGPTAPIkeyDatabase.isNotEmpty()) Icons.Default.Save else Icons.Default.Add,
                tooltip = if (chatGPTAPIkeyDatabase.isNotEmpty()) getStringResource("info.save.chat.gpt") else getStringResource(
                    "info.enable.chat.gpt"
                ),
                function = {
                    scope.launch {
                        if (chatGPTAPIkeyDatabase.isEmpty()) {
                            settingsSource.add(
                                identifier = AIKey.CHATGPT.name,
                                value = chatGPTAPIkey
                            )
                        } else {
                            settingsSource.update(
                                identifier = AIKey.CHATGPT.name,
                                value = chatGPTAPIkey
                            )
                        }
                    }
                    onMessage(
                        InfoManagerData(
                            message = getStringResource("info.message.edit.chat.gpt")
                        )
                    )
                }
            )

            if (chatGPTAPIkeyDatabase.isNotEmpty()) {
                TooltipIconButton(
                    modifier = Modifier.padding(end = 8.dp),
                    tint = darkBlue,
                    icon = Icons.Default.Remove,
                    tooltip = getStringResource("info.disable.chat.gpt"),
                    function = {
                        scope.launch {
                            settingsSource.delete(AIKey.CHATGPT.name)
                            onMessage(
                                InfoManagerData(
                                    message = getStringResource("info.message.remove.chat.gpt"),
                                    color = darkRed
                                )
                            )
                        }
                    }
                )
            }
        }
    }
}