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
import data.repository.settings.SettingsSource
import notifications.InfoManagerData
import ui.composable.elements.DividerColored
import ui.composable.elements.SelectableRow
import utils.getStringResource

@Composable
fun AISection(
    settingsSource: SettingsSource,
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
            getTitle = { type ->
                when (type) {
                    AIType.OLLAMA -> getStringResource("info.ollama.title")
                    AIType.OPEN_API -> getStringResource("info.openapi.title")
                }
            }
        )

        DividerColored(
            paddingValues = PaddingValues(bottom = 16.dp),
        )

        when (selectedInfoType) {
            AIType.OLLAMA -> Ollama(
                settingsSource = settingsSource,
                onMessage = onMessage
            )

            AIType.OPEN_API -> OpenApi(
                settingsSource = settingsSource,
                onMessage = onMessage
            )
        }
    }
}

private enum class AIType {
    OLLAMA, OPEN_API
}