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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jskako.droidsense.generated.resources.Res
import com.jskako.droidsense.generated.resources.info_adb_hint
import com.jskako.droidsense.generated.resources.info_adb_info
import com.jskako.droidsense.generated.resources.info_cancel
import com.jskako.droidsense.generated.resources.info_next
import com.jskako.droidsense.generated.resources.info_scrcpy_hint
import com.jskako.droidsense.generated.resources.info_scrcpy_info
import data.keys.SettingsKey
import data.repository.settings.SettingsSource
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import ui.composable.elements.OutlinedButton
import ui.composable.elements.SelectableText

@Composable
fun VariablesSection(
    title: String,
    description: String,
    cancelButtonEnabled: Boolean,
    settingsSource: SettingsSource,
    navigateToMainScreen: () -> Unit,
) {
    var adbPath by remember { mutableStateOf("") }
    var scrcpyPath by remember { mutableStateOf("") }

    val adbDatabasePath by settingsSource.get(SettingsKey.ADB.name).collectAsState(initial = "")
    val scrcpyDatabasePath by settingsSource.get(SettingsKey.SCRCPY.name).collectAsState(initial = "")
    val scope = rememberCoroutineScope()

    LaunchedEffect(adbDatabasePath, scrcpyDatabasePath) {
        adbPath = adbDatabasePath
        scrcpyPath = scrcpyDatabasePath
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(16.dp)
        ) {
            if (title.isNotEmpty()) {
                Text(
                    text = title,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            if (description.isNotEmpty()) {
                Text(
                    textAlign = TextAlign.Center,
                    text = description,
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.height(24.dp))
            }

            SelectableText(
                modifier = Modifier.fillMaxWidth(),
                text = adbPath,
                infoText = Res.string.info_adb_info,
                hintText = Res.string.info_adb_hint,
                onValueChanged = {
                    adbPath = it
                }
            )
            Spacer(modifier = Modifier.height(8.dp))
            SelectableText(
                modifier = Modifier.fillMaxWidth(),
                text = scrcpyPath,
                hintText = Res.string.info_scrcpy_hint,
                infoText = Res.string.info_scrcpy_info,
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
                        text = stringResource(Res.string.info_cancel),
                        onClick = {

                        },
                        modifier = Modifier.wrapContentSize()
                    )

                    Spacer(modifier = Modifier.width(8.dp))
                }

                OutlinedButton(
                    text = stringResource(Res.string.info_next),
                    enabled = adbPath.isNotEmpty(),
                    onClick = {
                        scope.launch {
                            settingsSource.update(
                                identifier = SettingsKey.ADB.name,
                                value = adbPath.trim()
                            )
                            scrcpyPath.trim().also {
                                if (it.isNotEmpty()) {
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