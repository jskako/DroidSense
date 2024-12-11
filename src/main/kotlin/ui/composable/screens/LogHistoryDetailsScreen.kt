package ui.composable.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.FolderOpen
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jskako.droidsense.generated.resources.Res
import com.jskako.droidsense.generated.resources.info_export_logs
import com.jskako.droidsense.generated.resources.info_search
import data.model.items.LogItem
import kotlinx.coroutines.launch
import notifications.InfoManager
import org.jetbrains.compose.resources.stringResource
import ui.composable.elements.DividerColored
import ui.composable.elements.ListWithScrollbar
import ui.composable.elements.iconButtons.TooltipIconButton
import ui.composable.elements.log.LogCard
import ui.composable.sections.info.FunctionIconData
import ui.composable.sections.info.InfoSection
import utils.Colors.transparentTextFieldDefault
import utils.EMPTY_STRING
import utils.exportToFile
import utils.openFolderAtPath

@Composable
fun LogHistoryDetailsScreen(
    logs: List<LogItem>,
) {

    val scope = rememberCoroutineScope()
    var searchText by remember { mutableStateOf(EMPTY_STRING) }
    val infoManager = remember { InfoManager() }
    var exportPath by remember { mutableStateOf<String?>(null) }
    val filteredLogs = logs.filter { log ->
        (searchText.isEmpty() || log.text.contains(searchText, ignoreCase = true))
    }

    Column(
        modifier = Modifier
            .padding(
                horizontal = 4.dp,
            )
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        InfoSection(
            onCloseClicked = { infoManager.clearInfoMessage() },
            message = infoManager.infoManagerData.value.message,
            color = infoManager.infoManagerData.value.color,
            onExtraClicked = exportPath?.let {
                FunctionIconData(
                    function = {
                        scope.launch {
                            infoManager.showMessage(
                                infoManagerData = openFolderAtPath(path = it),
                                scope = scope
                            )
                        }
                    },
                    icon = Icons.Default.FolderOpen
                )
            },
            buttonVisible = infoManager.infoManagerData.value.buttonVisible
        )

        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {

            TextField(
                value = searchText,
                colors = transparentTextFieldDefault,
                singleLine = true,
                onValueChange = {
                    searchText = it
                },
                placeholder = { Text(stringResource(Res.string.info_search)) },
            )

            Spacer(modifier = Modifier.weight(1f))

            TooltipIconButton(
                icon = Icons.Default.FileDownload,
                tooltip = Res.string.info_export_logs,
                function = {
                    scope.launch {
                        buildString {
                            logs.forEach { log ->
                                appendLine(log.toString())
                            }
                        }.exportToFile().also {
                            exportPath = it.path
                            infoManager.showMessage(
                                infoManagerData = it.infoManagerData,
                                scope = scope
                            )
                        }
                    }
                }
            )
        }

        DividerColored()

        ListWithScrollbar(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 8.dp),
            lazyModifier = Modifier.padding(end = 15.dp),
            content = {
                items(filteredLogs) { item ->
                    LogCard(
                        item = item
                    )
                }
            }
        )
    }
}