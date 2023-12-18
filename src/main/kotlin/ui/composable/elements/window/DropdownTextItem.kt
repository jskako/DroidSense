package ui.composable.elements.window

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun DropdownTextItem(
    list: List<String>,
    text: String,
    onItemSelected: (String) -> Unit,
    descriptionText: String
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedItem by remember { mutableStateOf(text) }
    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = descriptionText,
            modifier = Modifier
                .wrapContentSize(),
            style = TextStyle(
                fontWeight = FontWeight.Bold
            )
        )

        Spacer(modifier = Modifier.width(4.dp))

        Column {
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                list.forEach { property ->
                    DropdownMenuItem(
                        onClick = {
                            selectedItem = property
                            expanded = false
                            onItemSelected(property)
                        }
                    ) {
                        Text(text = property)
                    }
                }
            }
        }

        Text(
            text = selectedItem,
            modifier = Modifier
                .wrapContentSize()
                .clickable { expanded = true }
        )
    }
}