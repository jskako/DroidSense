package ui.composable.elements.device

import adb.DeviceDetails
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import notifications.InfoManagerData
import ui.application.WindowStateManager

@Composable
fun DeviceView(
    adbPath: String,
    scrCpyPath: String,
    devices: List<DeviceDetails>,
    onMessage: (InfoManagerData) -> Unit,
    windowStateManager: WindowStateManager
) {
    LazyColumn(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        modifier = Modifier.padding(top = 8.dp),
    ) {
        items(devices) { device ->
            DeviceCard(
                adbPath = adbPath,
                scrCpyPath = scrCpyPath,
                device = device,
                onMessage = onMessage,
                windowStateManager = windowStateManager
            )
        }
    }
}