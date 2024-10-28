package ui.composable.elements.log


import adb.log.LogData
import adb.log.LogLevel
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import java.util.UUID


@Composable
fun LogView(
    logs: List<LogData>,
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
                (filteredText.isEmpty() || log.log.contains(filteredText, ignoreCase = true))
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        LazyColumn(
            modifier = Modifier.padding(end = 15.dp),
            state = listState
        ) {
            items(
                if (reversedLogs) filteredLogs.reversed() else filteredLogs
            ) { item ->
                LogCard(
                    item = item,
                    fontSize = fontSize,
                    onClicked = {
                        onLogSelected(item.uuid)
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