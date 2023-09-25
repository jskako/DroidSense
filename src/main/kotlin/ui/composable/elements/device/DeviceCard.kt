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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import ui.composable.elements.BasicText
import utils.DEFAULT_PHONE_IMAGE
import utils.IMAGES_DIRECTORY
import utils.getImageBitmap

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
                    text = "Device State: ${device.state}",
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}