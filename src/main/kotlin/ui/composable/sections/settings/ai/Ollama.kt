package ui.composable.sections.settings.ai

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import data.model.ai.ollama.OllamaModelItem
import data.repository.ai.ollama.model.OllamaModelSource
import data.repository.ai.ollama.url.OllamaUrlSource
import kotlinx.coroutines.launch
import notifications.InfoManagerData
import ui.composable.elements.ButtonRow
import ui.composable.elements.ListWithScrollbar
import ui.composable.elements.settings.DeleteEditRowCard
import utils.getStringResource

@Composable
fun Ollama(
    ollamaUrlSource: OllamaUrlSource,
    ollamaModelSource: OllamaModelSource,
    onMessage: (InfoManagerData) -> Unit
) {
    val scope = rememberCoroutineScope()
    var selectedURL by remember { mutableStateOf("") }

    val urls by ollamaUrlSource.get(context = scope.coroutineContext).collectAsState(initial = emptyList())
    val models by ollamaModelSource.by(url = selectedURL, context = scope.coroutineContext)
        .collectAsState(initial = emptyList())

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        UrlColumn(
            ollamaUrlSource = ollamaUrlSource,
            ollamaModelSource = ollamaModelSource,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            urls = urls,
            selectedURL = selectedURL,
            onUrlAdd = { input ->
                scope.launch { ollamaUrlSource.add(url = input) }
            },
            onUrlSelect = { url -> selectedURL = url },
            onMessage = onMessage
        )

        ModelColumn(
            ollamaModelSource = ollamaModelSource,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            models = models,
            selectedURL = selectedURL,
            onModelAdd = { input ->
                scope.launch {
                    ollamaModelSource.add(
                        ollamaModelItem = OllamaModelItem(
                            url = selectedURL,
                            model = input
                        )
                    )
                }
            },
            onMessage = onMessage
        )
    }
}

@Composable
private fun UrlColumn(
    ollamaUrlSource: OllamaUrlSource,
    ollamaModelSource: OllamaModelSource,
    modifier: Modifier,
    urls: List<String>,
    selectedURL: String,
    onUrlAdd: (String) -> Unit,
    onUrlSelect: (String) -> Unit,
    onMessage: (InfoManagerData) -> Unit
) {
    val scope = rememberCoroutineScope()

    Column(
        modifier = modifier
    ) {
        ButtonRow(
            modifier = Modifier.fillMaxWidth(),
            hintText = getStringResource("info.url.add"),
            onClick = onUrlAdd
        )

        ListWithScrollbar(
            contentPadding = PaddingValues(end = 20.dp),
            content = {
                items(urls) { url ->
                    DeleteEditRowCard(
                        text = url,
                        editTitle = getStringResource("info.edit.url"),
                        onEdit = {
                            scope.launch {
                                ollamaUrlSource.update(
                                    url = url,
                                    value = it
                                )
                                ollamaModelSource.updateUrls(
                                    url = url,
                                    value = it
                                )
                                onMessage(
                                    InfoManagerData(
                                        message = "${getStringResource("info.modify.url.message")} $url -> $it",
                                    )
                                )
                            }
                        },
                        onDelete = {},
                        onSelected = { onUrlSelect(url) },
                        isSelected = url == selectedURL
                    )
                }
            }
        )
    }
}

@Composable
private fun ModelColumn(
    ollamaModelSource: OllamaModelSource,
    modifier: Modifier,
    models: List<String>,
    selectedURL: String,
    onModelAdd: (String) -> Unit,
    onMessage: (InfoManagerData) -> Unit
) {

    val scope = rememberCoroutineScope()

    Column(
        modifier = modifier
    ) {
        ButtonRow(
            modifier = Modifier.fillMaxWidth(),
            hintText = getStringResource("info.model.add"),
            enabled = selectedURL.isNotBlank(),
            onClick = onModelAdd
        )

        ListWithScrollbar(
            contentPadding = PaddingValues(end = 20.dp),
            content = {
                items(models) { model ->
                    DeleteEditRowCard(
                        text = model,
                        editTitle = getStringResource("info.edit.model"),
                        onEdit = {
                            scope.launch {
                                ollamaModelSource.update(
                                    url = selectedURL,
                                    model = model,
                                    value = it
                                )
                                onMessage(
                                    InfoManagerData(
                                        message = "${getStringResource("info.modify.model.message")} $model -> $it",
                                    )
                                )
                            }
                        },
                        onDelete = {}
                    )
                }
            }
        )
    }
}
