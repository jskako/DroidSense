package ui.composable.sections.history

import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import data.model.items.NameItem
import data.repository.log.LogHistorySource
import data.repository.name.NameSource
import kotlinx.coroutines.launch
import notifications.InfoManagerData
import ui.composable.elements.DividerColored
import ui.composable.elements.history.NameCard
import ui.composable.elements.window.TextDialog
import utils.Colors.transparentTextFieldDefault
import utils.EMPTY_STRING
import utils.getStringResource
import java.util.UUID

@Composable
fun LogHistorySection(
    nameSource: NameSource,
    logHistorySource: LogHistorySource,
    onMessage: (InfoManagerData) -> Unit
) {

    val scope = rememberCoroutineScope()
    val nameItems by nameSource.by(context = scope.coroutineContext).collectAsState(initial = emptyList())
    var selectedNameItem by remember {
        mutableStateOf(
            NameItem(
                uuid = UUID(0, 0),
                name = ""
            )
        )
    }
    var deleteInProgress by remember { mutableStateOf(false) }
    val listState = rememberLazyListState()
    var showDialog by remember { mutableStateOf(false) }
    var searchText by remember { mutableStateOf(EMPTY_STRING) }

    val filteredNames = nameItems.filter { nameItem ->
        searchText.isEmpty()
                || nameItem.name.contains(searchText, ignoreCase = true)
                || nameItem.uuid.toString().contains(searchText, ignoreCase = true)
    }

    if (showDialog) {
        TextDialog(
            title = getStringResource("info.delete.log.title"),
            description = buildString {
                appendLine(getStringResource("info.delete.log.description"))
                appendLine(selectedNameItem.uuid)
            },
            onConfirmRequest = {
                showDialog = false
                scope.launch {
                    logHistorySource.deleteBy(selectedNameItem.uuid)
                    nameSource.deleteBy(selectedNameItem.uuid)
                    deleteInProgress = false
                }
            },
            onDismissRequest = {
                deleteInProgress = false
                showDialog = false
            }
        )
    }

    Column(
        modifier = Modifier
            .padding(
                horizontal = 4.dp,
                vertical = 16.dp
            )
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        TextField(
            value = searchText,
            colors = transparentTextFieldDefault,
            singleLine = true,
            onValueChange = {
                searchText = it
            },
            placeholder = { Text(getStringResource("info.search")) },
            modifier = Modifier
                .fillMaxWidth()
        )

        DividerColored()

        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            LazyColumn(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                modifier = Modifier.padding(top = 8.dp),
                state = listState
            ) {
                items(filteredNames) { nameItem ->
                    NameCard(
                        nameItem = nameItem,
                        onClick = {

                        },
                        onDelete = {
                            selectedNameItem = nameItem
                            showDialog = true
                        },
                        onMessage = {
                            onMessage(it)
                        },
                        deleteInProgress = deleteInProgress,
                        onDeleteInProgress = {
                            deleteInProgress = it
                        }
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