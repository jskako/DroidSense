package ui.composable.sections.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import data.keys.SettingsKey
import data.repository.settings.SettingsSource
import kotlinx.coroutines.launch
import ui.composable.elements.OutlinedButton
import ui.composable.elements.SelectableText
import utils.getStringResource

@Composable
fun GeneralSection(
    settingsSource: SettingsSource
) {

    val scope = rememberCoroutineScope()
    val settingsToSave = remember { mutableStateMapOf<SaveSetting, () -> Unit>() }
    var adbPath by remember { mutableStateOf(settingsSource.get(SettingsKey.ADB.name)) }
    val scrcpyPath by remember { mutableStateOf(settingsSource.get(SettingsKey.SCRCPY.name)) }

    Column(
        modifier = Modifier
            .padding(vertical = 16.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SelectableText(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth(),
            text = adbPath,
            infoText = getStringResource("info.adb.info"),
            hintText = getStringResource("info.adb.hint"),
            onValueChanged = { changedPath ->
                adbPath = changedPath
                changedPath.trim().also {
                    if (settingsSource.get(SettingsKey.ADB.name) != it) {
                        settingsToSave[SaveSetting.ADB_PATH] = {
                            scope.launch {
                                settingsSource.update(
                                    identifier = SettingsKey.ADB.name,
                                    value = it
                                )
                            }
                        }
                    } else {
                        settingsToSave.remove(SaveSetting.ADB_PATH)
                    }
                }
            }
        )

        Spacer(modifier = Modifier.weight(1f))

        OutlinedButton(
            text = "Save",
            enabled = settingsToSave.isNotEmpty(),
            onClick = {
                settingsToSave.forEach { (_, u) ->
                    u.invoke()
                }
            },
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .align(Alignment.End)
                .wrapContentSize()
        )
    }
}

private enum class SaveSetting {
    ADB_PATH, SCRCPY_PATH
}