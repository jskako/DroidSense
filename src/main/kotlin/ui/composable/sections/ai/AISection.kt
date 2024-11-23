package ui.composable.sections.ai

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Message
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import data.model.ai.ollama.OllamaMessage
import data.model.ai.ollama.OllamaRole
import data.network.NetworkModule
import data.repository.ai.ollama.OllamaNetworkRepositoryImpl
import domain.ollama.usecases.OllamaResponseUseCase
import notifications.InfoManager
import ui.application.WindowStateManager
import ui.composable.elements.ListWithScrollbar
import ui.composable.sections.info.InfoSection
import utils.Colors.darkBlue
import utils.getStringResource

@Composable
fun AISection(
    windowStateManager: WindowStateManager
) {

    LaunchedEffect(Unit) {
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
    }

    val infoManager = remember { InfoManager() }

    Scaffold(
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = {

                },
                containerColor = darkBlue,
                contentColor = Color.White,
                icon = { Icon(Icons.AutoMirrored.Filled.Message, getStringResource("info.new.chat")) },
                text = { Text(text = getStringResource("info.new.chat")) },
            )
        }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {

            InfoSection(
                onCloseClicked = { infoManager.clearInfoMessage() },
                message = infoManager.infoManagerData.value.message,
                color = infoManager.infoManagerData.value.color
            )

            ListWithScrollbar(
                lazyModifier = Modifier.padding(top = 8.dp),
                contentPadding = PaddingValues(
                    start = 16.dp,
                    end = 16.dp,
                    top = 8.dp,
                    bottom = 80.dp
                ),
                content = {
                    items(emptyList<String>()) { app ->

                    }
                }
            )
        }
    }
}
