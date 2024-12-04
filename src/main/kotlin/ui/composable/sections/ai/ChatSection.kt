package ui.composable.sections.ai

import androidx.compose.foundation.LocalScrollbarStyle
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import data.model.ai.AIItem
import data.model.ai.AIType
import data.model.ai.ollama.AiRole
import data.model.mappers.toOllamaMessage
import data.network.NetworkModule
import data.repository.ai.ollama.OllamaNetworkRepositoryImpl
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import notifications.InfoManagerData
import ui.composable.elements.DividerColored
import ui.composable.elements.ListWithScrollbar
import ui.composable.elements.ai.ChatCard
import ui.composable.elements.iconButtons.TooltipIconButton
import ui.composable.elements.window.Sources
import utils.Colors.darkBlue
import utils.Colors.darkRed
import utils.Colors.lightGray
import utils.Colors.transparentTextFieldDefault
import utils.DATABASE_DATETIME
import utils.EMPTY_STRING
import utils.getStringResource
import utils.getTimeStamp
import java.util.UUID

@Composable
fun ChatSection(
    sources: Sources,
    uuid: UUID,
    onMessage: (InfoManagerData) -> Unit,
) {

    val httpClient = NetworkModule.provideHttpClient()
    val aiRepository = OllamaNetworkRepositoryImpl(httpClient)
    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollState()
    val listState = rememberLazyListState()
    var message by remember { mutableStateOf(EMPTY_STRING) }
    var inProgress by remember { mutableStateOf(false) }
    var textFieldHeight by remember { mutableStateOf(0) }
    var currentJob by remember { mutableStateOf<Job?>(null) }

    fun resetProgress() {
        message = ""
        inProgress = false
    }

    val history by sources
        .aiHistorySource
        .history(context = scope.coroutineContext, uuid = uuid)
        .collectAsState(initial = emptyList())

    val messages by remember(history) {
        mutableStateOf(
            history.map { aiItem ->
                aiItem.toOllamaMessage()
            }.toTypedArray()
        )
    }

    sources
        .aiHistorySource
        .history(context = scope.coroutineContext, uuid = uuid)
        .map { aiItems ->
            aiItems.map { it.toOllamaMessage() }.toTypedArray()
        }
        .collectAsState(initial = emptyArray())

    LaunchedEffect(message) {
        scrollState.animateScrollTo(scrollState.maxValue)
    }

    LaunchedEffect(history) {
        if (history.isNotEmpty()) {
            listState.animateScrollToItem(history.lastIndex)
        }

        messages.lastOrNull()?.let {
            if (it.role == AiRole.USER) {
                currentJob = scope.launch {
                    aiRepository.getChatResponse(
                        model = "gemma2",
                        messages = messages
                    ).fold(
                        onSuccess = { response ->
                            sources.aiHistorySource.add(
                                AIItem(
                                    uuid = uuid,
                                    deviceSerialNumber = "",
                                    aiType = AIType.OLLAMA,
                                    url = "",
                                    role = response.role,
                                    message = response.content,
                                    dateTime = getTimeStamp(DATABASE_DATETIME),
                                    model = "gemma2"
                                )
                            )
                            resetProgress()
                        },
                        onFailure = { error ->
                            resetProgress()
                            onMessage(
                                InfoManagerData(
                                    message = "${getStringResource("info.error.ai.message")} $error",
                                    color = darkRed
                                )
                            )
                        }
                    )
                }
            }
        }
    }

    /*val filteredNames = nameItems.filter { nameItem ->
        val matchesSearchText = searchText.isEmpty() ||
                nameItem.name.contains(searchText, ignoreCase = true) ||
                nameItem.sessionUuid.toString().contains(searchText, ignoreCase = true)

        val matchesSerialNumber = deviceSerialNumber?.let { nonNullSerial ->
            nameItem.deviceSerialNumber?.contains(nonNullSerial, ignoreCase = true) == true
        } ?: true

        matchesSearchText && matchesSerialNumber
    }*/

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        DividerColored()

        ListWithScrollbar(
            modifier = Modifier
                .weight(0.9f)
                .fillMaxWidth(),
            lazyModifier = Modifier.padding(top = 8.dp),
            listState = listState,
            content = {
                items(history) { aiItem ->
                    ChatCard(
                        aiItem = aiItem,
                        onUpdate = {

                        }
                    )
                }
            }
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = darkBlue),
            verticalAlignment = Alignment.Bottom
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .heightIn(
                        min = TextFieldDefaults.MinHeight,
                        max = TextFieldDefaults.MinHeight * 4
                    )
            ) {
                TextField(
                    modifier = Modifier
                        .padding(horizontal = 20.dp, vertical = 8.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .fillMaxWidth()
                        .onSizeChanged { size ->
                            textFieldHeight = size.height
                        }
                        .verticalScroll(scrollState)
                        .background(color = Color.White),
                    enabled = !inProgress,
                    value = message,
                    onValueChange = { message = it },
                    placeholder = { Text(getStringResource("info.message.info")) },
                    singleLine = false,
                    colors = transparentTextFieldDefault
                )

                VerticalScrollbar(
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .height(with(LocalDensity.current) { textFieldHeight.toDp() })
                        .padding(horizontal = 24.dp, vertical = 8.dp)
                        .width(6.dp),
                    adapter = rememberScrollbarAdapter(
                        scrollState = scrollState
                    ),
                    style = LocalScrollbarStyle.current.copy(
                        hoverColor = darkBlue,
                        thickness = 6.dp
                    )
                )
            }

            if (inProgress) {
                TooltipIconButton(
                    modifier = Modifier.padding(end = 8.dp, bottom = 16.dp),
                    isEnabled = message.isNotBlank(),
                    tint = Color.White,
                    icon = Icons.Default.Stop,
                    tooltip = getStringResource("info.stop.process"),
                    function = {
                        currentJob?.cancel()
                        resetProgress()
                    }
                )
            } else {
                TooltipIconButton(
                    modifier = Modifier.padding(end = 8.dp, bottom = 16.dp),
                    isEnabled = message.isNotBlank(),
                    tint = if (message.isBlank()) lightGray else Color.White,
                    icon = Icons.AutoMirrored.Filled.Send,
                    tooltip = getStringResource("info.send.message"),
                    function = {
                        inProgress = true
                        scope.launch {
                            sources.aiHistorySource.add(
                                AIItem(
                                    uuid = uuid,
                                    deviceSerialNumber = "",
                                    aiType = AIType.OLLAMA,
                                    url = "",
                                    role = AiRole.USER,
                                    message = message,
                                    dateTime = getTimeStamp(DATABASE_DATETIME),
                                    model = "gemma2"
                                )
                            )
                        }
                    }
                )
            }
        }
    }
}