package ui.composable.elements.device

import adb.DeviceDetails
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ui.composable.elements.ButtonRow
import ui.composable.elements.CustomText

/*@Composable
fun DeviceCard(device: DeviceDetails) {
    Card(
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 8.dp)
            .fillMaxWidth(),
        elevation = 2.dp,
        backgroundColor = Color.Gray,
        shape = RoundedCornerShape(corner = CornerSize(16.dp))
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            val extra = mutableMapOf<String, () -> Unit>()
            CustomText(
                text = "${device.model} (${device.serialNumber})",
                fontSize = 18.sp,
                isBold = true
            )

            device.extra.also {
                if (it != null) {
                    it.functions?.forEach { function ->
                        extra[function.key] = { function.value() }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            ButtonRow(extra)
        }
    }
}*/

@Composable
fun DeviceCard(deviceDetails: DeviceDetails) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = 4.dp
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Model",
                    tint = Color.Gray,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = deviceDetails.model)
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Serial Number",
                    tint = Color.Gray,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = deviceDetails.serialNumber)
            }

            // Add similar rows for other properties with icons
            // You can customize icons, content descriptions, and tints as needed

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Device State: ${deviceDetails.state}",
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}
