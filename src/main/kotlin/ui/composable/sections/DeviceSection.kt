package ui.composable.sections

import adb.AdbManager.startListening
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
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
    startListening(rememberCoroutineScope())
    Row(
        modifier = modifier,
        verticalAlignment = verticalAlignment
    ) {
        DeviceView()
    }
}