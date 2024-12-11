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
import com.jskako.droidsense.generated.resources.Res
import com.jskako.droidsense.generated.resources.error_input
import com.jskako.droidsense.generated.resources.info_delete_model_description
import com.jskako.droidsense.generated.resources.info_delete_url_description
import com.jskako.droidsense.generated.resources.info_delete_url_message
import com.jskako.droidsense.generated.resources.info_edit_model
import com.jskako.droidsense.generated.resources.info_edit_url
import com.jskako.droidsense.generated.resources.info_model_add
import com.jskako.droidsense.generated.resources.info_model_delete_message
import com.jskako.droidsense.generated.resources.info_modify_model_message
import com.jskako.droidsense.generated.resources.info_modify_url_message
import com.jskako.droidsense.generated.resources.info_url_add
import data.ArgsText
import data.model.ai.AIModelItem
import data.model.ai.AIType
import data.repository.ai.model.ModelSource
import data.repository.ai.ollama.url.OllamaUrlSource
import kotlinx.coroutines.launch
import notifications.InfoManagerData
import org.jetbrains.compose.resources.stringResource
import ui.composable.elements.AddRow
import ui.composable.elements.ListWithScrollbar
import ui.composable.elements.settings.DeleteEditRowCard
import utils.OLLAMA_DEFAULT_API

@Composable
fun Ollama(
    ollamaUrlSource: OllamaUrlSource,
    modelSource: ModelSource,
    onMessage: (InfoManagerData) -> Unit
) {
    val scope = rememberCoroutineScope()
    var selectedURL by remember { mutableStateOf("") }

    val urls by ollamaUrlSource.get(context = scope.coroutineContext).collectAsState(initial = emptyList())
    val models by modelSource.by(url = selectedURL, context = scope.coroutineContext)
        .collectAsState(initial = emptyList())

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        UrlColumn(
            ollamaUrlSource = ollamaUrlSource,
            modelSource = modelSource,
            onDelete = {
                selectedURL = ""
            },
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            urls = urls,
            selectedURL = selectedURL,
            onUrlAdd = { input ->
                if (input.isNotBlank()) {
                    scope.launch {
                        ollamaUrlSource.add(url = "$input$OLLAMA_DEFAULT_API")
                    }
                } else {
                    onMessage(
                        InfoManagerData(
                            message = ArgsText(textResId = Res.string.error_input),
                        )
                    )
                }
            },
            onUrlSelect = { url -> selectedURL = url },
            onMessage = onMessage
        )

        ModelColumn(
            modelSource = modelSource,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            models = models,
            selectedURL = selectedURL,
            onModelAdd = { input ->
                if (input.isNotBlank()) {
                    scope.launch {
                        modelSource.add(
                            aiModelItem = AIModelItem(
                                url = selectedURL,
                                model = input,
                                aiType = AIType.OLLAMA,
                            )
                        )
                    }
                } else {
                    onMessage(
                        InfoManagerData(
                            message = ArgsText(textResId = Res.string.error_input),
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
    modelSource: ModelSource,
    modifier: Modifier,
    urls: List<String>,
    selectedURL: String,
    onUrlAdd: (String) -> Unit,
    onUrlSelect: (String) -> Unit,
    onDelete: () -> Unit,
    onMessage: (InfoManagerData) -> Unit
) {
    val scope = rememberCoroutineScope()

    Column(
        modifier = modifier
    ) {
        AddRow(
            modifier = Modifier.fillMaxWidth(),
            hintText = Res.string.info_url_add,
            onClick = onUrlAdd,
            additionalText = OLLAMA_DEFAULT_API
        )

        ListWithScrollbar(
            contentPadding = PaddingValues(end = 20.dp),
            content = {
                items(urls) { url ->
                    DeleteEditRowCard(
                        text = url,
                        editTitle = Res.string.info_edit_url,
                        deleteDialogDescription = "${stringResource(Res.string.info_delete_url_description)} $url",
                        onEdit = {
                            scope.launch {
                                ollamaUrlSource.update(
                                    url = url,
                                    value = it
                                )
                                modelSource.updateUrls(
                                    url = url,
                                    value = it
                                )
                                onMessage(
                                    InfoManagerData(
                                        message = ArgsText(
                                            textResId = Res.string.info_modify_url_message,
                                            formatArgs = listOf("$url -> $it")
                                        ),
                                    )
                                )
                            }
                        },
                        onDelete = {
                            scope.launch {
                                modelSource.deleteByUrl(url)
                                ollamaUrlSource.deleteBy(url)
                                onDelete()
                                onMessage(
                                    InfoManagerData(
                                        message = ArgsText(
                                            textResId = Res.string.info_delete_url_message,
                                            formatArgs = listOf(url)
                                        ),
                                    )
                                )
                            }
                        },
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
    modelSource: ModelSource,
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
        AddRow(
            modifier = Modifier.fillMaxWidth(),
            hintText = Res.string.info_model_add,
            enabled = selectedURL.isNotBlank(),
            onClick = onModelAdd
        )

        ListWithScrollbar(
            contentPadding = PaddingValues(end = 20.dp),
            content = {
                items(models) { model ->
                    DeleteEditRowCard(
                        text = model,
                        editTitle = Res.string.info_edit_model,
                        deleteDialogDescription = "${stringResource(Res.string.info_delete_model_description)} $selectedURL / $model",
                        onEdit = {
                            scope.launch {
                                modelSource.update(
                                    url = selectedURL,
                                    model = model,
                                    value = it
                                )
                                onMessage(
                                    InfoManagerData(
                                        message = ArgsText(
                                            textResId = Res.string.info_modify_model_message,
                                            formatArgs = listOf("$selectedURL / $model -> $selectedURL / $it")
                                        )
                                    )
                                )
                            }
                        },
                        onDelete = {
                            scope.launch {
                                modelSource.deleteBy(
                                    url = selectedURL,
                                    model = model
                                )
                                onMessage(
                                    InfoManagerData(
                                        message = ArgsText(
                                            textResId = Res.string.info_model_delete_message,
                                            formatArgs = listOf("$selectedURL / $model")
                                        )
                                    )
                                )
                            }
                        }
                    )
                }
            }
        )
    }
}
