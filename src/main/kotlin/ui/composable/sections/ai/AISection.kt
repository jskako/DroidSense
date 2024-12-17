package ui.composable.sections.ai

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Message
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.jskako.droidsense.generated.resources.Res
import com.jskako.droidsense.generated.resources.empty_string
import com.jskako.droidsense.generated.resources.info_ai_error
import com.jskako.droidsense.generated.resources.info_available_ai
import com.jskako.droidsense.generated.resources.info_clear_filter
import com.jskako.droidsense.generated.resources.info_delete_log_message
import com.jskako.droidsense.generated.resources.info_name_update_log_message
import com.jskako.droidsense.generated.resources.info_new_chat
import com.jskako.droidsense.generated.resources.info_search
import com.jskako.droidsense.generated.resources.title_ai_chat
import data.ArgsText
import data.model.ai.AIType
import kotlinx.coroutines.launch
import notifications.InfoManagerData
import org.jetbrains.compose.resources.stringResource
import ui.application.WindowExtra
import ui.application.WindowStateManager
import ui.application.navigation.WindowData
import ui.composable.elements.DividerColored
import ui.composable.elements.ListWithScrollbar
import ui.composable.elements.SelectionDialog
import ui.composable.elements.history.NameCard
import ui.composable.elements.iconButtons.TooltipIconButton
import ui.composable.elements.window.Sources
import ui.composable.screens.ChatScreen
import utils.Colors.darkBlue
import utils.Colors.darkRed
import utils.Colors.transparentTextFieldDefault
import utils.EMPTY_STRING

@Composable
fun AISection(
    windowStateManager: WindowStateManager,
    deviceSerialNumber: String? = null,
    sources: Sources,
    onMessage: (InfoManagerData) -> Unit,
) {

    val scope = rememberCoroutineScope()
    var searchText by remember { mutableStateOf(EMPTY_STRING) }
    val aiNameSource by remember { mutableStateOf(sources.aiNameSource) }
    val aiHistorySource by remember { mutableStateOf(sources.aiHistorySource) }
    val nameItems by aiNameSource.by(context = scope.coroutineContext).collectAsState(initial = emptyList())
    val aiTypes by sources.modelSource.types(context = scope.coroutineContext).collectAsState(initial = emptyList())
    var deleteInProgress by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }

    LaunchedEffect(aiTypes) {
        if (aiTypes.isEmpty()) {
            onMessage(
                InfoManagerData(
                    message = ArgsText(
                        textResId = Res.string.info_ai_error,
                    ),
                    color = darkRed,
                    duration = null
                )
            )
        }
    }

    if (showDialog) {
        SelectionDialog(
            title = Res.string.info_available_ai,
            options = aiTypes,
            onOptionSelected = { aiType ->
                windowStateManager.windowState?.openNewWindow?.let { newWindow ->
                    newWindow(
                        WindowData(
                            title = ArgsText(
                                textResId = Res.string.title_ai_chat,
                                formatArgs = listOf(aiType)
                            ),
                            icon = Icons.Default.Info,
                            windowExtra = WindowExtra(
                                screen = {
                                    ChatScreen(
                                        aiType = AIType.valueOf(aiType),
                                        sources = sources
                                    )
                                },
                                onClose = {}
                            )
                        )
                    )
                }
            },
            onDismissRequest = {
                showDialog = false
            }
        )
    }

    val filteredNames = nameItems.filter { nameItem ->
        val matchesSearchText = searchText.isEmpty() ||
                nameItem.name.contains(searchText, ignoreCase = true) ||
                nameItem.sessionUuid.toString().contains(searchText, ignoreCase = true)

        val matchesSerialNumber = deviceSerialNumber?.let { nonNullSerial ->
            nameItem.deviceSerialNumber?.contains(nonNullSerial, ignoreCase = true) == true
        } ?: true

        matchesSearchText && matchesSerialNumber
    }

    Scaffold(
        floatingActionButton = {
            if (aiTypes.isNotEmpty()) {
                ExtendedFloatingActionButton(
                    onClick = {
                        showDialog = true
                    },
                    containerColor = darkBlue,
                    contentColor = Color.White,
                    icon = { Icon(Icons.AutoMirrored.Filled.Message, stringResource(Res.string.info_new_chat)) },
                    text = { Text(text = stringResource(Res.string.info_new_chat)) },
                )
            }
        }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {

                deviceSerialNumber?.let {
                    if (it.isNotBlank()) {
                        Text(
                            text = it,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Gray
                        )

                        TooltipIconButton(
                            icon = Icons.Default.Close,
                            tooltip = Res.string.info_clear_filter,
                            function = {
                                //onFilterClear
                            }
                        )
                    }
                }

                TextField(
                    value = searchText,
                    colors = transparentTextFieldDefault,
                    singleLine = true,
                    onValueChange = {
                        searchText = it
                    },
                    placeholder = { Text(stringResource(Res.string.info_search)) },
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }

            DividerColored()

            ListWithScrollbar(
                lazyModifier = Modifier.padding(top = 8.dp),
                content = {
                    items(filteredNames.reversed()) { nameItem ->
                        NameCard(
                            name = nameItem.name,
                            uuid = nameItem.sessionUuid,
                            dateTime = nameItem.dateTime,
                            onClick = {
                                scope.launch {
                                    windowStateManager.windowState?.openNewWindow?.let { newWindow ->
                                        newWindow(
                                            WindowData(
                                                title = ArgsText(
                                                    textResId = Res.string.title_ai_chat,
                                                    formatArgs = listOf("${nameItem.aiType.name} - ${nameItem.name}")
                                                ),
                                                icon = Icons.Default.Info,
                                                windowExtra = WindowExtra(
                                                    screen = {
                                                        ChatScreen(
                                                            sources = sources,
                                                            uuid = nameItem.sessionUuid,
                                                            deviceSerialNumber = deviceSerialNumber,
                                                            aiType = nameItem.aiType
                                                        )
                                                    },
                                                    onClose = {}
                                                )
                                            )
                                        )
                                    }
                                }
                            },
                            onDelete = {
                                deleteInProgress = true
                                scope.launch {
                                    aiHistorySource.deleteBy(nameItem.sessionUuid)
                                    aiNameSource.deleteBy(nameItem.sessionUuid)
                                    deleteInProgress = false
                                    onMessage(
                                        InfoManagerData(
                                            message = ArgsText(
                                                textResId = Res.string.info_delete_log_message,
                                            )
                                        )
                                    )
                                }
                            },
                            onUpdate = {
                                aiNameSource.update(
                                    sessionUuid = nameItem.sessionUuid,
                                    name = it
                                )
                                onMessage(
                                    InfoManagerData(
                                        message = ArgsText(
                                            textResId = Res.string.info_name_update_log_message,
                                        )
                                    )
                                )
                            },
                            buttonsEnabled = !deleteInProgress,
                        )
                    }
                }
            )
        }
    }
}
