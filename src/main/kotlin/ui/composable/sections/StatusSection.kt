package ui.composable.sections

import adb.AdbDeviceManager.listeningStatus
import adb.AdbDeviceManager.manageListeningStatus
import adb.DeviceManager.devices
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import utils.getStringResource

@Composable
fun StatusSection() {
    val scope = rememberCoroutineScope()
    Box(
        modifier = Modifier
            .wrapContentWidth()
            .clickable { manageListeningStatus(scope) }
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Row {
            Text(
                text = "${getStringResource("info.status.general")}: ${listeningStatus.value.status()}",
                textAlign = TextAlign.Center,
            )

            if (devices.isNotEmpty()) {
                Spacer(modifier = Modifier.width(16.dp))

                Text(
                    text = "${getStringResource("info.device.number")}: ${devices.size}",
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}