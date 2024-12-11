package ui.composable.sections.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jskako.droidsense.generated.resources.Res
import com.jskako.droidsense.generated.resources.info_adb_hint
import com.jskako.droidsense.generated.resources.info_adb_info
import com.jskako.droidsense.generated.resources.info_reset
import com.jskako.droidsense.generated.resources.info_reset_general_description
import com.jskako.droidsense.generated.resources.info_reset_success
import com.jskako.droidsense.generated.resources.info_save
import com.jskako.droidsense.generated.resources.info_save_success
import com.jskako.droidsense.generated.resources.info_scrcpy_hint
import com.jskako.droidsense.generated.resources.info_scrcpy_info
import data.ArgsText
import data.keys.SettingsKey
import data.repository.settings.SettingsSource
import kotlinx.coroutines.launch
import notifications.InfoManagerData
import org.jetbrains.compose.resources.stringResource
import ui.composable.elements.OutlinedButton
import ui.composable.elements.SelectableText
import ui.composable.elements.window.TextDialog
import utils.ADB_PACKAGE
import utils.Colors.darkRed
import utils.SCRCPY_PACKAGE
import utils.findPath

@Composable
fun GeneralSection(
    settingsSource: SettingsSource,
    onMessage: (InfoManagerData) -> Unit
) {

    val scope = rememberCoroutineScope()
    val settingsToSave = remember { mutableStateMapOf<SaveSetting, () -> Unit>() }

    var adbPath by remember { mutableStateOf("") }
    val adbDatabasePath by settingsSource.get(SettingsKey.ADB.name).collectAsState(initial = "")

    var scrcpyPath by remember { mutableStateOf("") }
    val scrcpyDatabasePath by settingsSource.get(SettingsKey.SCRCPY.name).collectAsState(initial = "")

    var showDialog by remember { mutableStateOf(false) }

    if (showDialog) {
        TextDialog(
            title = ArgsText(textResId = Res.string.info_reset),
            description = ArgsText(textResId = Res.string.info_reset_general_description),
            onConfirmRequest = {
                showDialog = false
                scope.launch {
                    settingsSource.add(
                        identifier = SettingsKey.ADB.name,
                        value = ADB_PACKAGE.findPath()
                    )

                    settingsSource.add(
                        identifier = SettingsKey.SCRCPY.name,
                        value = SCRCPY_PACKAGE.findPath()
                    )

                    onMessage(
                        InfoManagerData(
                            message = ArgsText(textResId = Res.string.info_reset_success)
                        )
                    )
                }
            },
            onDismissRequest = {
                showDialog = false
            }
        )
    }

    LaunchedEffect(adbDatabasePath, scrcpyDatabasePath) {
        adbPath = adbDatabasePath
        scrcpyPath = scrcpyDatabasePath
    }

    Column(
        modifier = Modifier
            .padding(vertical = 32.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SelectableText(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth(),
            text = adbPath,
            infoText = Res.string.info_adb_info,
            hintText = Res.string.info_adb_hint,
            onValueChanged = { changedPath ->
                adbPath = changedPath
                changedPath.trim().also {
                    if (adbDatabasePath != it) {
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

        Spacer(modifier = Modifier.height(8.dp))

        SelectableText(
            modifier = Modifier
                .padding(top = 16.dp)
                .padding(horizontal = 16.dp)
                .fillMaxWidth(),
            text = scrcpyPath,
            hintText = Res.string.info_scrcpy_hint,
            infoText = Res.string.info_scrcpy_info,
            onValueChanged = { changedPath ->
                scrcpyPath = changedPath
                changedPath.trim().also {
                    if (scrcpyDatabasePath != it) {
                        settingsToSave[SaveSetting.SCRCPY_PATH] = {
                            scope.launch {
                                settingsSource.update(
                                    identifier = SettingsKey.SCRCPY.name,
                                    value = it
                                )
                            }
                        }
                    } else {
                        settingsToSave.remove(SaveSetting.SCRCPY_PATH)
                    }
                }
            }
        )

        Spacer(modifier = Modifier.weight(1f))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(
                space = 10.dp,
                alignment = Alignment.End
            )
        ) {

            OutlinedButton(
                text = stringResource(Res.string.info_reset),
                contentColor = darkRed,
                onClick = {
                    showDialog = true
                }
            )

            OutlinedButton(
                text = stringResource(Res.string.info_save),
                enabled = settingsToSave.isNotEmpty(),
                onClick = {
                    settingsToSave.forEach { (_, u) ->
                        u.invoke()
                    }
                    onMessage(
                        InfoManagerData(
                            message = ArgsText(textResId = Res.string.info_save_success)
                        )
                    )
                }
            )
        }
    }
}

private enum class SaveSetting {
    ADB_PATH, SCRCPY_PATH
}