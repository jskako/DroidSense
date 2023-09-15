package ui.composable.elements.logs

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import di.AppModule.provideLogManager

@Composable
fun LogsView() {
    val logs = provideLogManager().logs.value.reversed()
    LazyColumn(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        modifier = Modifier.padding(top = 8.dp),
    ) {
        items(logs) { log ->
            LogCard(log)
        }
    }
}