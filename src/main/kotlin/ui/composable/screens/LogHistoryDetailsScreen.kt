package ui.composable.screens

import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FileDownload
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
import data.model.items.LogItem
import kotlinx.coroutines.launch
import notifications.InfoManagerData
import ui.composable.elements.DividerColored
import ui.composable.elements.iconButtons.TooltipIconButton
import ui.composable.elements.log.LogCard
import utils.Colors.transparentTextFieldDefault
import utils.EMPTY_STRING
import utils.exportToFile
import utils.getStringResource

@Composable
fun LogHistoryDetailsScreen(
    logs: List<LogItem>,
    onMessage: (InfoManagerData) -> Unit
) {

    val scope = rememberCoroutineScope()
    var searchText by remember { mutableStateOf(EMPTY_STRING) }
    val listState = rememberLazyListState()

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
                placeholder = { Text(getStringResource("info.search")) },
            )

            Spacer(modifier = Modifier.weight(1f))

            TooltipIconButton(
                icon = Icons.Default.FileDownload,
                tooltip = getStringResource("info.export.logs"),
                function = {
                    scope.launch {
                        buildString {
                            logs.forEach { log ->
                                appendLine(log.toString())
                            }
                        }.exportToFile().also {
                            onMessage(it.infoManagerData)
                        }
                    }
                }
            )
        }

        DividerColored()

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 8.dp)
        ) {
            LazyColumn(
                modifier = Modifier.padding(end = 15.dp),
                state = listState
            ) {
                items(filteredLogs) { item ->
                    LogCard(
                        item = item
                    )
                }
            }
            VerticalScrollbar(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .fillMaxHeight()
                    .padding(end = 5.dp)
                    .width(15.dp),
                adapter = rememberScrollbarAdapter(
                    scrollState = listState
                )
            )
        }
    }
}