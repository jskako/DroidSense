package ui.composable.sections.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import data.repository.log.LogHistorySource

@Composable
fun DatabaseSection(
    logHistorySource: LogHistorySource
) {
    Column(
        modifier = Modifier
            .padding(vertical = 16.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

    }
}