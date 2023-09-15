package ui.composable.sections

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ui.composable.elements.device.DeviceView

@Composable
fun DeviceSection(
    modifier: Modifier = Modifier
        .fillMaxWidth()
        .padding(15.dp),
    verticalAlignment: Alignment.Vertical = Alignment.CenterVertically
) {
    Row(
        modifier = modifier,
        verticalAlignment = verticalAlignment
    ) {
        DeviceView()
    }
}