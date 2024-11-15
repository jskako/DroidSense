package ui.composable.elements

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
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
import data.repository.settings.SettingsSource
import kotlinx.coroutines.launch
import notifications.InfoManagerData
import ui.composable.elements.iconButtons.TooltipIconButton
import utils.Colors.darkBlue
import utils.Colors.darkRed
import utils.Colors.lightGray

@Composable
fun <T> SettingRow(
    key: T,
    settingsSource: SettingsSource,
    hintText: String,
    saveTooltip: String,
    enableTooltip: String,
    removeTooltip: String,
    editMessage: String,
    removeMessage: String,
    onMessage: (InfoManagerData) -> Unit,
    onKeyFound: (String) -> Unit
) {
    val scope = rememberCoroutineScope()
    var keyInput by remember { mutableStateOf("") }
    val keyDatabase by settingsSource.get(key.toString()).collectAsState(initial = "")
    val isButtonEnabled = keyInput.trim().isNotEmpty() && keyInput.trim() != keyDatabase.trim()

    LaunchedEffect(keyDatabase) {
        keyInput = keyDatabase
        onKeyFound(keyDatabase)
    }

    OutlinedText(
        text = keyInput,
        hintText = hintText,
        onValueChanged = { changedValue ->
            keyInput = changedValue
        },
        trailingIcon = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TooltipIconButton(
                    modifier = Modifier.then(
                        if (keyDatabase.isNotEmpty()) Modifier else Modifier.padding(end = 8.dp),
                    ),
                    isEnabled = isButtonEnabled,
                    tint = if (isButtonEnabled) darkBlue else lightGray,
                    icon = if (keyDatabase.isNotEmpty()) Icons.Default.Save else Icons.Default.Add,
                    tooltip = if (keyDatabase.isNotEmpty()) saveTooltip else enableTooltip,
                    function = {
                        scope.launch {
                            settingsSource.add(identifier = key.toString(), value = keyInput)
                        }
                        onMessage(InfoManagerData(message = editMessage))
                    }
                )

                if (keyDatabase.isNotEmpty()) {
                    TooltipIconButton(
                        modifier = Modifier.padding(end = 8.dp),
                        tint = darkBlue,
                        icon = Icons.Default.Remove,
                        tooltip = removeTooltip,
                        function = {
                            scope.launch {
                                settingsSource.delete(key.toString())
                                onMessage(InfoManagerData(message = removeMessage, color = darkRed))
                            }
                        }
                    )
                }
            }
        }
    )
}