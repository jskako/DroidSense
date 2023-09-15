package ui.composable.elements.device

import adb.DeviceManager.devices
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun DeviceView() {
    LaunchedEffect(devices) {
        println("Devices size: ${devices.size}")
        println("Devices size: ${devices}")
    }
    LazyColumn(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        modifier = Modifier.padding(top = 8.dp),
    ) {
        items(devices) { device ->
            println("Here")
            DeviceCard(device)
        }
    }
}