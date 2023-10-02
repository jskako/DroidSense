package ui.composable.elements.log


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
import androidx.compose.ui.unit.dp
import log.LogData


@Composable
fun LogView(logs: List<LogData>) {
    val listState = rememberLazyListState()

    LaunchedEffect(logs.size) {
        listState.animateScrollToItem(logs.size)
    }

    Box(
        modifier = Modifier.fillMaxSize()
            .padding(start = 15.dp)
    ) {
        LazyColumn(
            modifier = Modifier.padding(end = 15.dp),
            state = listState
        ) {
            items(logs) { item ->
                LogCard(item)
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