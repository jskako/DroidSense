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
import data.repository.ai.AIHistorySource
import data.repository.name.ai.AiNameSource
import kotlinx.coroutines.launch
import notifications.InfoManagerData
import ui.application.WindowExtra
import ui.application.WindowStateManager
import ui.application.navigation.WindowData
import ui.composable.elements.DividerColored
import ui.composable.elements.ListWithScrollbar
import ui.composable.elements.history.NameCard
import ui.composable.elements.iconButtons.TooltipIconButton
import ui.composable.screens.ChatScreen
import utils.Colors.darkBlue
import utils.Colors.transparentTextFieldDefault
import utils.EMPTY_STRING
import utils.getStringResource

@Composable
fun AISection(
    windowStateManager: WindowStateManager,
    deviceSerialNumber: String? = null,
    aiHistorySource: AIHistorySource,
    aiNameSource: AiNameSource,
    onMessage: (InfoManagerData) -> Unit,
) {

    val scope = rememberCoroutineScope()
    var searchText by remember { mutableStateOf(EMPTY_STRING) }
    val nameItems by aiNameSource.by(context = scope.coroutineContext).collectAsState(initial = emptyList())
    var deleteInProgress by remember { mutableStateOf(false) }

    val filteredNames = nameItems.filter { nameItem ->
        val matchesSearchText = searchText.isEmpty() ||
                nameItem.name.contains(searchText, ignoreCase = true) ||
                nameItem.sessionUuid.toString().contains(searchText, ignoreCase = true)

        val matchesSerialNumber = deviceSerialNumber?.let { nonNullSerial ->
            nameItem.deviceSerialNumber?.contains(nonNullSerial, ignoreCase = true) == true
        } ?: true

        matchesSearchText && matchesSerialNumber
    }

    /*LaunchedEffect(Unit) {
        // TODO - This block is just for test, delete later
        val httpClient = NetworkModule.provideHttpClient()
        val chatGPTRepository = OllamaNetworkRepositoryImpl(httpClient)
        val getResponseUseCase = OllamaResponseUseCase(chatGPTRepository)
        println(
            "OpenAI answer: ${
                getResponseUseCase.invoke(
                    "gemma2",
                    arrayOf(
                        OllamaMessage(
                            role = OllamaRole.USER,
                            content = "Call me Josip."
                        ),
                        OllamaMessage(
                            role = OllamaRole.ASSISTANT,
                            content = "Sure, from now on I'll call you Josip."
                        ),
                        OllamaMessage(
                            role = OllamaRole.USER,
                            content = "What is my name? Are you able to generate image from it?"
                        ),
                    )
                )
            }"
        )
    }*/

    Scaffold(
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = {
                    windowStateManager.windowState?.openNewWindow?.let { newWindow ->
                        newWindow(
                            WindowData(
                                title = "",
                                icon = Icons.Default.Info,
                                windowExtra = WindowExtra(
                                    screen = {
                                        ChatScreen()
                                    },
                                    onClose = {}
                                )
                            )
                        )
                    }
                },
                containerColor = darkBlue,
                contentColor = Color.White,
                icon = { Icon(Icons.AutoMirrored.Filled.Message, getStringResource("info.new.chat")) },
                text = { Text(text = getStringResource("info.new.chat")) },
            )
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
                            tooltip = getStringResource("info.clear.filter"),
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
                    placeholder = { Text(getStringResource("info.search")) },
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
                                windowStateManager.windowState?.openNewWindow?.let { newWindow ->
                                    newWindow(
                                        WindowData(
                                            title = buildString {
                                                "${nameItem.name} (${nameItem.deviceSerialNumber})"
                                                append(nameItem.name)
                                                nameItem.deviceSerialNumber?.let {
                                                    append(" $it")
                                                }
                                            },
                                            icon = Icons.Default.Info,
                                            windowExtra = WindowExtra(
                                                screen = {
                                                    /*
                                                    val logs by logHistorySource.by(
                                                        context = scope.coroutineContext,
                                                        sessionUuid = nameItem.sessionUuid
                                                    ).collectAsState(initial = emptyList())

                                                    LogHistoryDetailsScreen(
                                                        logs = logs
                                                    )*/
                                                },
                                                onClose = {}
                                            )
                                        )
                                    )
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
                                            message = getStringResource("info.delete.log.message")
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
                                        message = getStringResource("info.name.update.log.message")
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
