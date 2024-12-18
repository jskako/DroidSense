package ui.composable.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.unit.dp
import data.model.ai.AIType
import data.model.items.AiNameItem
import kotlinx.coroutines.flow.flowOf
import notifications.InfoManager
import ui.composable.elements.DividerColored
import ui.composable.elements.DropdownItem
import ui.composable.elements.window.Sources
import ui.composable.sections.ai.ChatSection
import ui.composable.sections.info.InfoSection
import utils.DATABASE_DATETIME
import utils.EXPORT_NAME_TIMESTAMP
import utils.getTimeStamp
import java.util.UUID

@Composable
fun ChatScreen(
    sources: Sources,
    uuid: UUID? = null,
    deviceSerialNumber: String? = null,
    startMessage: String? = null,
    aiType: AIType
) {

    val infoManager = remember { InfoManager() }
    val scope = rememberCoroutineScope()
    val sessionUuid by remember { mutableStateOf(uuid ?: UUID.randomUUID()) }

    val urls by sources.ollamaUrlSource.get(context = scope.coroutineContext).collectAsState(initial = emptyList())
    var selectedUrl by remember { mutableStateOf("") }

    val models by (selectedUrl.takeIf { it.isNotBlank() }
        ?.let { validUrl ->
            sources.modelSource.by(context = scope.coroutineContext, validUrl)
        } ?: flowOf(emptyList()))
        .collectAsState(initial = emptyList())

    var selectedModel by remember { mutableStateOf("") }

    LaunchedEffect(urls) {
        if (urls.isNotEmpty()) {
            selectedUrl = urls.first()
        }
    }

    LaunchedEffect(selectedUrl, models) {
        if (selectedUrl.isNotBlank() && models.isNotEmpty()) {
            selectedModel = models.first()
        }
    }

    LaunchedEffect(Unit) {
        if (uuid == null) {
            sources.aiNameSource.add(
                AiNameItem(
                    sessionUuid = sessionUuid,
                    name = "${sessionUuid}_${
                        getTimeStamp(
                            EXPORT_NAME_TIMESTAMP
                        )
                    }",
                    dateTime = getTimeStamp(DATABASE_DATETIME),
                    deviceSerialNumber = deviceSerialNumber,
                    aiType = aiType
                )
            )
        }
    }

    Column {
        InfoSection(
            onCloseClicked = { infoManager.clearInfoMessage() },
            message = infoManager.infoManagerData.value.message,
            color = infoManager.infoManagerData.value.color
        )

        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            DropdownItem(
                list = urls,
                text = selectedUrl,
                onItemSelected = { item ->
                    selectedUrl = item
                },
                enabled = urls.isNotEmpty(),
                buttonText = selectedUrl,
            )

            DropdownItem(
                list = models,
                text = selectedModel,
                onItemSelected = { item ->
                    selectedModel = item
                },
                enabled = models.isNotEmpty(),
                buttonText = selectedModel,
            )
        }

        DividerColored()

        ChatSection(
            sources = sources,
            startMessage = startMessage,
            uuid = sessionUuid,
            selectedUrl = selectedUrl,
            selectedModel = selectedModel,
            deviceSerialNumber = deviceSerialNumber ?: "",
            onMessage = {
                infoManager.showMessage(
                    infoManagerData = it,
                    scope = scope
                )
            }
        )
    }
}