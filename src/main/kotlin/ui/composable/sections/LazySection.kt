package ui.composable.sections

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun LazySection(
    modifier: Modifier = Modifier
        .fillMaxWidth()
        .padding(start = 15.dp, end = 15.dp),
    view: @Composable () -> Unit,
    verticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
) {
    Row(
        modifier = modifier,
        verticalAlignment = verticalAlignment
    ) {
        view()
    }
}