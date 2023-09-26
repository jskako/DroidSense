package ui.composable.elements.device

import adb.DeviceDetails
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import ui.application.WindowStateManager.windowState
import ui.composable.elements.BasicText
import ui.composable.elements.OutlinedButton
import ui.composable.screens.LogScreen
import utils.DEFAULT_PHONE_IMAGE
import utils.IMAGES_DIRECTORY
import utils.getImageBitmap
import utils.getStringResource
import utils.startScrCpy

@Composable
fun DeviceCard(
    device: DeviceDetails
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = 4.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                bitmap = getImageBitmap("$IMAGES_DIRECTORY/$DEFAULT_PHONE_IMAGE"),
                contentDescription = "",
                modifier = Modifier.size(48.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))
            Column {
                val chunkedItems = rememberUpdatedState(device.toDeviceCardInfoList()).value.chunked(2)

                for (chunk in chunkedItems) {
                    Row {
                        for (item in chunk) {
                            BasicText(
                                value = "${item.description}: ${item.value}"
                            )

                            Spacer(modifier = Modifier.width(16.dp))
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "${getStringResource("info.device.state")}: ${device.state}",
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(8.dp))
            }

            Column(
                modifier = Modifier.padding(start = 16.dp, end = 16.dp)
            ) {
                OutlinedButton(
                    text = getStringResource("info.share.screen"),
                    onClick = { startScrCpy(device.serialNumber) }
                )

                Spacer(modifier = Modifier.height(16.dp))

                getStringResource("info.log.manager").also { title ->
                    OutlinedButton(
                        text = title,
                        onClick = { windowState?.openNewWindow?.let { it(title, Icons.Default.Info) { LogScreen(device) } } }
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedButton(
                    text = getStringResource("info.log.options"),
                    onClick = { }
                )
            }
        }
    }
}