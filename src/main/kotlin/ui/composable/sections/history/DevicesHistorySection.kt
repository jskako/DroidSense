package ui.composable.sections.history

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.items
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
import data.model.items.DeviceItem
import data.model.items.DeviceItem.Companion.emptyDeviceItem
import kotlinx.coroutines.launch
import notifications.InfoManagerData
import ui.composable.elements.DividerColored
import ui.composable.elements.ListWithScrollbar
import ui.composable.elements.history.DeviceCard
import ui.composable.elements.window.Sources
import ui.composable.elements.window.TextDialog
import utils.Colors.transparentTextFieldDefault
import utils.EMPTY_STRING
import utils.getStringResource

@Composable
fun DevicesHistorySection(
    sources: Sources,
    onMessage: (InfoManagerData) -> Unit,
    onDeviceItemClicked: (DeviceItem) -> Unit,
) {

    val scope = rememberCoroutineScope()
    val deviceItems by sources.deviceSource.by(context = scope.coroutineContext).collectAsState(initial = emptyList())
    var deleteInProgress by remember { mutableStateOf(false) }
    var selectedDeviceItem by remember { mutableStateOf(emptyDeviceItem) }
    var showDialog by remember { mutableStateOf(false) }
    var searchText by remember { mutableStateOf(EMPTY_STRING) }

    val filteredDevices = deviceItems.filter { deviceItem ->
        searchText.isEmpty() ||
                deviceItem.brand?.contains(searchText, ignoreCase = true) == true ||
                deviceItem.model.toString().contains(searchText, ignoreCase = true) ||
                deviceItem.manufacturer.toString().contains(searchText, ignoreCase = true) ||
                deviceItem.serialNumber.contains(searchText, ignoreCase = true)
    }

    if (showDialog) {
        TextDialog(
            title = getStringResource("info.delete.device.title"),
            description = buildString {
                appendLine(getStringResource("info.delete.device.description"))
                appendLine("$selectedDeviceItem ${selectedDeviceItem.serialNumber}")
            },
            onConfirmRequest = {
                showDialog = false
                val serialNumber = selectedDeviceItem.serialNumber
                scope.launch {
                    sources.logHistorySource.let { logSource ->
                        logSource.uuids(serialNumber = serialNumber).forEach { uuid ->
                            logSource.deleteBy(uuid)
                        }
                    }

                    sources.logNameSource.let { nameSource ->
                        nameSource.uuids(serialNumber = serialNumber).forEach { uuid ->
                            nameSource.deleteBy(uuid)
                        }
                    }
                    sources.deviceSource.deleteBy(serialNumber = serialNumber)

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

        ListWithScrollbar(
            lazyModifier = Modifier.padding(top = 8.dp),
            content = {
                items(filteredDevices) { deviceItem ->
                    DeviceCard(
                        deviceItem = deviceItem,
                        onClick = {
                            onDeviceItemClicked(deviceItem)
                        },
                        onDelete = {
                            selectedDeviceItem = deviceItem
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
        )
    }
}