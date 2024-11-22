package ui.composable.elements.log


import adb.log.LogLevel
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import data.model.items.LogItem
import ui.composable.elements.ListWithScrollbar
import java.util.UUID


@Composable
fun LogView(
    logs: List<LogItem>,
    logLevel: LogLevel,
    filteredText: String,
    scrollToEnd: Boolean,
    reversedLogs: Boolean,
    fontSize: TextUnit,
    onLogSelected: (UUID) -> Unit,
) {
    val listState = rememberLazyListState()

    if (scrollToEnd) {
        LaunchedEffect(logs.size) {
            listState.scrollToItem(logs.size)
        }
    }

    val filteredLogs = logs.filter { log ->
        log.level.ordinal <= logLevel.ordinal &&
                (filteredText.isEmpty() || log.text.contains(filteredText, ignoreCase = true))
    }

    ListWithScrollbar(
        lazyModifier = Modifier.padding(end = 15.dp),
        listState = listState,
        content = {
            items(
                if (reversedLogs) filteredLogs.reversed() else filteredLogs
            ) { item ->
                LogCard(
                    item = item,
                    fontSize = fontSize,
                    onClicked = {
                        onLogSelected(item.itemUuid)
                    }
                )
            }
        }
    )
}