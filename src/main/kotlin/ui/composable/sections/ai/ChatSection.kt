package ui.composable.sections.ai

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.ui.unit.dp
import notifications.InfoManagerData
import ui.composable.elements.DividerColored
import ui.composable.elements.ListWithScrollbar
import ui.composable.elements.iconButtons.TooltipIconButton
import utils.Colors.darkBlue
import utils.Colors.transparentTextFieldDefault
import utils.EMPTY_STRING
import utils.getStringResource

@Composable
fun ChatSection(
    deviceSerialNumber: String? = null,
    onMessage: (InfoManagerData) -> Unit,
) {

    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollState()
    var searchText by remember { mutableStateOf(EMPTY_STRING) }
    //val nameItems by aiNameSource.by(context = scope.coroutineContext).collectAsState(initial = emptyList())
    var deleteInProgress by remember { mutableStateOf(false) }
    val mockedList = listOf<String>(
        "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13",
        "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13",
        "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13",
        "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13",
        "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13",
    )

    LaunchedEffect(searchText) {
        scrollState.animateScrollTo(scrollState.maxValue)
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
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        DividerColored()

        ListWithScrollbar(
            modifier = Modifier
                .weight(0.9f)
                .fillMaxSize(),
            lazyModifier = Modifier.padding(top = 8.dp),
            content = {
                items(mockedList) {
                    Text(
                        text = it,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        )

        Row(
            modifier = Modifier.background(color = darkBlue),
            verticalAlignment = Alignment.Bottom
        ) {

            TextField(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .weight(1f)
                    .clip(RoundedCornerShape(16.dp))
                    .heightIn(max = TextFieldDefaults.MinHeight * 4)
                    .verticalScroll(scrollState)
                    .fillMaxWidth()
                    .background(color = Color.White),
                value = searchText,
                onValueChange = { searchText = it },
                placeholder = { Text(getStringResource("info.search")) },
                singleLine = false,
                colors = transparentTextFieldDefault
            )

            TooltipIconButton(
                modifier = Modifier.padding(end = 8.dp, bottom = 16.dp),
                tint = Color.White,
                icon = Icons.AutoMirrored.Filled.Send,
                tooltip = getStringResource("info.edit.name"),
                function = {

                }
            )
        }
    }
}
