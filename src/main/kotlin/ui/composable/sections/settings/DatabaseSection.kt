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
import data.DATABASE_NAME
import kotlinx.coroutines.launch
import notifications.InfoManagerData
import ui.composable.elements.OutlinedButton
import ui.composable.elements.OutlinedText
import ui.composable.elements.iconButtons.TooltipIconButton
import ui.composable.elements.window.Sources
import ui.composable.elements.window.TextDialog
import utils.Colors.darkBlue
import utils.Colors.darkRed
import utils.getStringResource
import utils.openFolderAtPath
import kotlin.io.path.Path
import kotlin.io.path.absolutePathString

@Composable
fun DatabaseSection(
    sources: Sources,
    onMessage: (InfoManagerData) -> Unit
) {

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
            hintText = getStringResource("info.database.location"),
            onValueChanged = {},
            readOnly = true,
            trailingIcon = {
                TooltipIconButton(
                    modifier = Modifier.padding(end = 16.dp),
                    tint = darkBlue,
                    icon = Icons.Default.FolderOpen,
                    tooltip = getStringResource("info.show.folder"),
                    function = {
                        openFolderAtPath(databasePath.substringBeforeLast("/"))
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

    var dialogTitle by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }
    var dialogDescription by remember { mutableStateOf("") }
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
            text = getStringResource("info.remove.settings.title"),
            contentColor = darkRed,
            onClick = {
                dialogTitle = getStringResource("info.remove.settings.title")
                dialogDescription = getStringResource("info.remove.settings.description")
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
                                message = getStringResource("info.remove.settings.success"),
                            )
                        )
                    }
                }
                showDialog = true
            },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedButton(
            text = getStringResource("info.remove.devices.title"),
            contentColor = darkRed,
            onClick = {
                dialogTitle = getStringResource("info.remove.devices.title")
                dialogDescription = getStringResource("info.remove.devices.description")
                onDialogConfirm = {
                    scope.launch {
                        sources.logHistorySource.deleteAll()
                        sources.logNameSource.deleteAll()
                        sources.deviceSource.deleteAll()
                        onMessage(
                            InfoManagerData(
                                message = getStringResource("info.remove.devices.success"),
                            )
                        )
                    }
                }
                showDialog = true
            },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedButton(
            text = getStringResource("info.remove.logs.title"),
            contentColor = darkRed,
            onClick = {
                dialogTitle = getStringResource("info.remove.logs.title")
                dialogDescription = getStringResource("info.remove.logs.description")
                onDialogConfirm = {
                    scope.launch {
                        sources.logHistorySource.deleteAll()
                        sources.logNameSource.deleteAll()
                        onMessage(
                            InfoManagerData(
                                message = getStringResource("info.remove.logs.success"),
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