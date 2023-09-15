package ui.composable.elements.device

import adb.DeviceDetails
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ui.composable.elements.ButtonRow
import ui.composable.elements.CustomText

@Composable
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
                text = "${device.model} (${device.serialNumber}})",
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
}