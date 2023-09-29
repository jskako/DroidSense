package ui.composable.elements.log


import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import log.LogData


@Composable
fun LogView(logs: List<LogData>) {
    val listState = rememberLazyListState()

    LaunchedEffect(logs.size) {
        listState.animateScrollToItem(logs.size)
    }

    LazyColumn(state = listState) {
        items(logs) { item ->
            Text(text = item.log, modifier = Modifier.fillMaxWidth())
        }
    }
}