package ui.composable.sections

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import data.keys.SettingsKey
import data.repository.SettingsSource
import kotlinx.coroutines.launch
import ui.composable.elements.OutlinedButton
import ui.composable.elements.SelectableText
import utils.Colors.darkBlue
import utils.getStringResource

@Composable
fun VariablesSection(
    title: String,
    description: String,
    cancelButtonEnabled: Boolean,
    settingsSource: SettingsSource,
    navigateToMainScreen: () -> Unit,
) {
    var adbPath by remember { mutableStateOf(settingsSource.get(SettingsKey.ADB.name)) }
    var scrcpyPath by remember { mutableStateOf(settingsSource.get(SettingsKey.SCRCPY.name)) }
    val scope = rememberCoroutineScope()

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(16.dp)
        ) {
            title.also {
                if (it.isNotEmpty()) {
                    Text(
                        text = it,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            description.also {
                if (it.isNotEmpty()) {
                    Text(
                        textAlign = TextAlign.Center,
                        text = description,
                        fontSize = 16.sp
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }

            SelectableText(
                modifier = Modifier.fillMaxWidth(),
                text = adbPath,
                infoText = getStringResource("info.adb.info"),
                hintText = getStringResource("info.adb.hint"),
                onValueChanged = {
                    adbPath = it
                }
            )
            Spacer(modifier = Modifier.height(8.dp))
            SelectableText(
                modifier = Modifier.fillMaxWidth(),
                text = scrcpyPath,
                hintText = getStringResource("info.scrcpy.hint"),
                infoText = getStringResource("info.scrcpy.info"),
                onValueChanged = {
                    scrcpyPath = it
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {

                if (cancelButtonEnabled) {
                    OutlinedButton(
                        text = getStringResource("info.cancel"),
                        color = darkBlue,
                        onClick = {

                        },
                        modifier = Modifier.wrapContentSize()
                    )

                    Spacer(modifier = Modifier.width(8.dp))
                }

                val isValid = adbPath.isNotEmpty() && scrcpyPath.isNotEmpty()

                OutlinedButton(
                    text = getStringResource("info.next"),
                    color = if (isValid) darkBlue else Color.Gray,
                    enabled = isValid,
                    onClick = {
                        scope.launch {
                            adbPath.trim().also {
                                if (settingsSource.get(SettingsKey.ADB.name) != it) {
                                    settingsSource.update(
                                        identifier = SettingsKey.ADB.name,
                                        value = it
                                    )
                                }
                            }
                            scrcpyPath.trim().also {
                                if (settingsSource.get(SettingsKey.SCRCPY.name) != it) {
                                    settingsSource.update(
                                        identifier = SettingsKey.SCRCPY.name,
                                        value = it
                                    )
                                }
                            }
                        }
                        navigateToMainScreen()
                    },
                    modifier = Modifier.wrapContentSize()
                )
            }
        }
    }
}