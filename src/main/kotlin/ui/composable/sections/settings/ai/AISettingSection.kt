package ui.composable.sections.settings.ai

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jskako.droidsense.generated.resources.Res
import com.jskako.droidsense.generated.resources.info_ollama_title
import com.jskako.droidsense.generated.resources.info_openapi_title
import data.model.ai.AIType
import data.repository.ai.model.ModelSource
import data.repository.ai.ollama.url.OllamaUrlSource
import data.repository.settings.SettingsSource
import notifications.InfoManagerData
import ui.composable.elements.DividerColored
import ui.composable.elements.SelectableRow

@Composable
fun AISettingSection(
    settingsSource: SettingsSource,
    modelSource: ModelSource,
    ollamaUrlSource: OllamaUrlSource,
    onMessage: (InfoManagerData) -> Unit
) {

    var selectedInfoType by remember { mutableStateOf(AIType.OLLAMA) }

    Column(
        modifier = Modifier
            .padding(
                vertical = 16.dp,
                horizontal = 32.dp
            )
            .fillMaxSize()
    ) {

        SelectableRow(
            enumValues = AIType.entries.toTypedArray(),
            selectedValue = selectedInfoType,
            onSelect = { selectedInfoType = it },
            onTitle = { type ->
                when (type) {
                    AIType.OLLAMA -> Res.string.info_ollama_title
                    AIType.OPEN_API -> Res.string.info_openapi_title
                }
            }
        )

        DividerColored(
            paddingValues = PaddingValues(bottom = 16.dp),
        )

        when (selectedInfoType) {
            AIType.OLLAMA -> Ollama(
                modelSource = modelSource,
                ollamaUrlSource = ollamaUrlSource,
                onMessage = onMessage
            )

            AIType.OPEN_API -> OpenApi(
                settingsSource = settingsSource,
                onMessage = onMessage
            )
        }
    }
}