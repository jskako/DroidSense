package ui.composable.sections

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ui.composable.elements.DropdownItem
import utils.DEVICE_PACKAGES
import utils.getDevicePropertyList

@Composable
fun LogStatusSection(
    serialNumber: String,
    text: String
) {
    Box(
        modifier = Modifier
            .wrapContentWidth()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column {
            Text(
                text = text,
                textAlign = TextAlign.Center,
            )

            Spacer(modifier = Modifier.height(16.dp))

            DropdownItem(
                list = getDevicePropertyList(serialNumber, DEVICE_PACKAGES),
                onItemSelected = { selectedItem ->
                    println("selectedItem: $selectedItem")
                }
            )
        }
    }
}