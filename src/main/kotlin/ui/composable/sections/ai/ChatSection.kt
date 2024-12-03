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
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import notifications.InfoManagerData
import ui.composable.elements.DividerColored
import ui.composable.elements.ListWithScrollbar
import ui.composable.elements.ai.ChatCard
import ui.composable.elements.iconButtons.TooltipIconButton
import utils.Colors.darkBlue
import utils.Colors.transparentTextFieldDefault
import utils.DATABASE_DATETIME
import utils.EMPTY_STRING
import utils.getStringResource
import utils.getTimeStamp
import java.util.UUID

@Composable
fun ChatSection(
    deviceSerialNumber: String? = null,
    onMessage: (InfoManagerData) -> Unit,
) {

    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollState()
    val listState = rememberLazyListState()
    var searchText by remember { mutableStateOf(EMPTY_STRING) }
    //val nameItems by aiNameSource.by(context = scope.coroutineContext).collectAsState(initial = emptyList())
    var deleteInProgress by remember { mutableStateOf(false) }
    var textFieldHeight by remember { mutableStateOf(0) }

    LaunchedEffect(searchText) {
        scrollState.animateScrollTo(scrollState.maxValue)
    }

    LaunchedEffect(mockedList) {
        listState.animateScrollToItem(mockedList.lastIndex)
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
                items(mockedList) { aiItem ->
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
                    value = searchText,
                    onValueChange = { searchText = it },
                    placeholder = { Text(getStringResource("info.search")) },
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

            TooltipIconButton(
                modifier = Modifier.padding(end = 8.dp, bottom = 16.dp),
                tint = Color.White,
                icon = Icons.AutoMirrored.Filled.Send,
                tooltip = getStringResource("info.edit.name"),
                function = {
                    // Action here
                }
            )
        }
    }
}


private val mockedList = buildList {
    repeat(10) {
        add(
            AIItem(
                uuid = UUID.randomUUID(),
                deviceSerialNumber = "20234",
                aiType = AIType.OLLAMA,
                url = "",
                model = "",
                role = AiRole.USER,
                message = "ListWithScrollbar(\n" +
                        "            modifier = Modifier\n" +
                        "                .weight(0.9f)\n" +
                        "                .fillMaxSize(),\n" +
                        "            lazyModifier = Modifier.padding(top = 8.dp),\n" +
                        "            content = {\n" +
                        "                items(mockedList) {\n" +
                        "                    Text(\n" +
                        "                        text = it,\n" +
                        "                        modifier = Modifier.fillMaxWidth()\n" +
                        "                    )\n" +
                        "                }\n" +
                        "            }\n" +
                        "        )\n" +
                        "\n" +
                        "        Row(\n" +
                        "            modifier = Modifier.background(color = darkBlue),\n" +
                        "            verticalAlignment = Alignment.Bottom\n" +
                        "        ) {\n" +
                        "\n" +
                        "            Box(\n" +
                        "                modifier = Modifier\n" +
                        "                    .weight(1f)\n" +
                        "            ) {\n" +
                        "                TextField(\n" +
                        "                    modifier = Modifier\n" +
                        "                        .padding(horizontal = 16.dp, vertical = 8.dp)\n" +
                        "                        .clip(RoundedCornerShape(16.dp))\n" +
                        "                        .heightIn(max = TextFieldDefaults.MinHeight * 4)\n" +
                        "                        .verticalScroll(scrollState)\n" +
                        "                        .fillMaxWidth()\n" +
                        "                        .background(color = Color.White),\n" +
                        "                    value = searchText,\n" +
                        "                    onValueChange = { searchText = it },\n" +
                        "                    placeholder = { Text(getStringResource(\"info.search\")) },\n" +
                        "                    singleLine = false,\n" +
                        "                    colors = transparentTextFieldDefault\n" +
                        "                )\n" +
                        "\n" +
                        "                VerticalScrollbar(\n" +
                        "                    modifier = Modifier\n" +
                        "                        .align(Alignment.CenterEnd)\n" +
                        "                        .padding(end = 5.dp)\n" +
                        "                        .width(15.dp),\n" +
                        "                    adapter = rememberScrollbarAdapter(\n" +
                        "                        scrollState = scrollState\n" +
                        "                    )\n" +
                        "                )\n" +
                        "            }\n" +
                        "\n" +
                        "            TooltipIconButton(\n" +
                        "                modifier = Modifier.padding(end = 8.dp, bottom = 16.dp),\n" +
                        "                tint = Color.White,\n" +
                        "                icon = Icons.AutoMirrored.Filled.Send,\n" +
                        "                tooltip = getStringResource(\"info.edit.name\"),\n" +
                        "                function = {\n" +
                        "\n" +
                        "                }\n" +
                        "            )\n" +
                        "        }\n" +
                        "    }",
                dateTime = getTimeStamp(DATABASE_DATETIME)
            )
        )

        add(
            AIItem(
                uuid = UUID.randomUUID(),
                deviceSerialNumber = "20234",
                aiType = AIType.OLLAMA,
                url = "",
                model = "",
                role = AiRole.ASSISTANT,
                message = "Your code snippet doesn't explicitly reveal your name, as it primarily involves a Compose layout implementation for UI components like a ListWithScrollbar, TextField, and TooltipIconButton. If you're referring to something like a username or identity in the UI, it might be coming from the mockedList, searchText, or a resource string like getStringResource(\"info.edit.name\").\n" +
                        "\n" +
                        "If you meant something specific like:\n" +
                        "\n" +
                        "A string resource displaying your name.\n" +
                        "The data in mockedList containing your name.\n" +
                        "You can clarify or check those values directly.\n" +
                        "\n" +
                        "If you're asking in a conversational sense, I don’t have your name unless you provide it! \uD83D\uDE0A Let me know if I can help with anything else in this code.",
                dateTime = getTimeStamp(DATABASE_DATETIME)
            )
        )
    }
}