package ui.composable.sections.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FolderOpen
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jskako.droidsense.generated.resources.Res
import com.jskako.droidsense.generated.resources.info_database_location
import com.jskako.droidsense.generated.resources.info_remove_devices_description
import com.jskako.droidsense.generated.resources.info_remove_devices_success
import com.jskako.droidsense.generated.resources.info_remove_devices_title
import com.jskako.droidsense.generated.resources.info_remove_logs_description
import com.jskako.droidsense.generated.resources.info_remove_logs_success
import com.jskako.droidsense.generated.resources.info_remove_logs_title
import com.jskako.droidsense.generated.resources.info_remove_settings_description
import com.jskako.droidsense.generated.resources.info_remove_settings_success
import com.jskako.droidsense.generated.resources.info_remove_settings_title
import com.jskako.droidsense.generated.resources.info_show_folder
import data.ArgsText
import data.DATABASE_NAME
import kotlinx.coroutines.launch
import notifications.InfoManagerData
import org.jetbrains.compose.resources.stringResource
import ui.composable.elements.OutlinedButton
import ui.composable.elements.OutlinedText
import ui.composable.elements.iconButtons.TooltipIconButton
import ui.composable.elements.window.Sources
import ui.composable.elements.window.TextDialog
import utils.Colors.darkBlue
import utils.Colors.darkRed
import utils.openFolderAtPath
import kotlin.io.path.Path
import kotlin.io.path.absolutePathString

@Composable
fun DatabaseSection(
    sources: Sources,
    onMessage: (InfoManagerData) -> Unit
) {

    val scope = rememberCoroutineScope()
    val databasePath by remember {
        mutableStateOf(
            Path(System.getProperty("user.home"), "DroidSense").resolve(DATABASE_NAME).absolutePathString()
        )
    }

    Column(
        modifier = Modifier
            .padding(top = 32.dp)
            .padding(horizontal = 32.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        OutlinedText(
            modifier = Modifier.fillMaxWidth(),
            text = databasePath,
            hintText = Res.string.info_database_location,
            onValueChanged = {},
            readOnly = true,
            trailingIcon = {
                TooltipIconButton(
                    modifier = Modifier.padding(end = 16.dp),
                    tint = darkBlue,
                    icon = Icons.Default.FolderOpen,
                    tooltip = Res.string.info_show_folder,
                    function = {
                        scope.launch {
                            openFolderAtPath(databasePath.substringBeforeLast("/"))
                        }
                    }
                )
            }
        )

        NukeDatabaseControll(
            sources = sources,
            onMessage = onMessage
        )
    }
}

@Composable
private fun NukeDatabaseControll(
    sources: Sources,
    onMessage: (InfoManagerData) -> Unit
) {

    var dialogTitle by remember { mutableStateOf(ArgsText()) }
    var showDialog by remember { mutableStateOf(false) }
    var dialogDescription by remember { mutableStateOf(ArgsText()) }
    var onDialogConfirm by remember { mutableStateOf({}) }

    if (showDialog) {
        TextDialog(
            title = dialogTitle,
            description = dialogDescription,
            onConfirmRequest = {
                showDialog = false
                onDialogConfirm()
            },
            onDismissRequest = {
                showDialog = false
            }
        )
    }

    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 36.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        OutlinedButton(
            text = stringResource(Res.string.info_remove_settings_title),
            contentColor = darkRed,
            onClick = {
                dialogTitle = ArgsText(textResId = Res.string.info_remove_settings_title)
                dialogDescription = ArgsText(textResId = Res.string.info_remove_settings_description)
                onDialogConfirm = {
                    scope.launch {
                        sources.settingsSource.deleteAll()
                        sources.logHistorySource.deleteAll()
                        sources.logNameSource.deleteAll()
                        sources.deviceSource.deleteAll()
                        sources.modelSource.deleteAll()
                        sources.ollamaUrlSource.deleteAll()
                        onMessage(
                            InfoManagerData(
                                message = ArgsText(textResId = Res.string.info_remove_settings_success),
                            )
                        )
                    }
                }
                showDialog = true
            },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedButton(
            text = stringResource(Res.string.info_remove_devices_title),
            contentColor = darkRed,
            onClick = {
                dialogTitle = ArgsText(textResId = Res.string.info_remove_devices_title)
                dialogDescription = ArgsText(textResId = Res.string.info_remove_devices_description)
                onDialogConfirm = {
                    scope.launch {
                        sources.logHistorySource.deleteAll()
                        sources.logNameSource.deleteAll()
                        sources.deviceSource.deleteAll()
                        onMessage(
                            InfoManagerData(
                                message = ArgsText(textResId = Res.string.info_remove_devices_success),
                            )
                        )
                    }
                }
                showDialog = true
            },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedButton(
            text = stringResource(Res.string.info_remove_logs_title),
            contentColor = darkRed,
            onClick = {
                dialogTitle = ArgsText(Res.string.info_remove_logs_title)
                dialogDescription = ArgsText(Res.string.info_remove_logs_description)
                onDialogConfirm = {
                    scope.launch {
                        sources.logHistorySource.deleteAll()
                        sources.logNameSource.deleteAll()
                        onMessage(
                            InfoManagerData(
                                message = ArgsText(textResId = Res.string.info_remove_logs_success)
                            )
                        )
                    }
                }
                showDialog = true
            },
            modifier = Modifier.fillMaxWidth()
        )
    }
}