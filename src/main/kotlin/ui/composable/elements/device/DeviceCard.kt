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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import ui.composable.elements.BasicText
import utils.DEFAULT_PHONE_IMAGE
import utils.IMAGES_DIRECTORY
import utils.getImageBitmap

@Composable
fun DeviceCard(deviceDetails: DeviceDetails) {
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
                contentDescription = "Device Image",
                modifier = Modifier.size(48.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Row {
                    BasicText(
                        value = "Model: ${deviceDetails.model ?: ""}"
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    BasicText(
                        value = "Serial Number: ${deviceDetails.serialNumber}"
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Device State: ${deviceDetails.state}",
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}