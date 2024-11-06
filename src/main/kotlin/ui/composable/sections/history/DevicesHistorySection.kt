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
import data.model.items.PhoneItem
import data.model.items.PhoneItem.Companion.emptyPhoneItem
import kotlinx.coroutines.launch
import notifications.InfoManagerData
import ui.composable.elements.DividerColored
import ui.composable.elements.history.PhoneCard
import ui.composable.elements.window.Sources
import ui.composable.elements.window.TextDialog
import utils.Colors.transparentTextFieldDefault
import utils.EMPTY_STRING
import utils.getStringResource

@Composable
fun DevicesHistorySection(
    sources: Sources,
    onMessage: (InfoManagerData) -> Unit,
    onPhoneItemClicked: (PhoneItem) -> Unit,
) {

    val scope = rememberCoroutineScope()
    val phoneItems by sources.phoneSource.by(context = scope.coroutineContext).collectAsState(initial = emptyList())
    var deleteInProgress by remember { mutableStateOf(false) }
    val listState = rememberLazyListState()
    var selectedPhoneItem by remember { mutableStateOf(emptyPhoneItem) }
    var showDialog by remember { mutableStateOf(false) }
    var searchText by remember { mutableStateOf(EMPTY_STRING) }

    val filteredPhones = phoneItems.filter { phoneItem ->
        searchText.isEmpty() ||
                phoneItem.brand?.contains(searchText, ignoreCase = true) == true ||
                phoneItem.model.toString().contains(searchText, ignoreCase = true) ||
                phoneItem.manufacturer.toString().contains(searchText, ignoreCase = true) ||
                phoneItem.serialNumber.contains(searchText, ignoreCase = true)
    }

    if (showDialog) {
        TextDialog(
            title = getStringResource("info.delete.device.title"),
            description = buildString {
                appendLine(getStringResource("info.delete.device.description"))
                appendLine("$selectedPhoneItem ${selectedPhoneItem.serialNumber}")
            },
            onConfirmRequest = {
                showDialog = false
                val serialNumber = selectedPhoneItem.serialNumber
                scope.launch {
                    sources.logHistorySource.let { logSource ->
                        logSource.uuids(serialNumber = serialNumber).forEach { uuid ->
                            logSource.deleteBy(uuid)
                        }
                    }

                    sources.nameSource.let { nameSource ->
                        nameSource.uuids(serialNumber = serialNumber).forEach { uuid ->
                            nameSource.deleteBy(uuid)
                        }
                    }
                    sources.phoneSource.deleteBy(serialNumber = serialNumber)

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
                items(filteredPhones) { phoneItem ->
                    PhoneCard(
                        phoneItem = phoneItem,
                        onClick = {
                            onPhoneItemClicked(phoneItem)
                        },
                        onDelete = {
                            selectedPhoneItem = phoneItem
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