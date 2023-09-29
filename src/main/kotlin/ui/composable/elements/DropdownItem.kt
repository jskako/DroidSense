package ui.composable.elements

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
fun DropdownItem(
    list: List<String>,
    text: String,
    onItemSelected: (String) -> Unit,
    visible: Boolean = true,
    clickableText: Boolean = false,
    buttonText: String,
    descriptionText: String
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedItem by remember { mutableStateOf(text) }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        if (visible) {
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

                OutlinedButton(
                    text = buttonText,
                    onClick = { expanded = true },
                    modifier = Modifier.wrapContentSize()
                )
            }
        } else {
            Text(
                text = descriptionText,
                style = TextStyle(fontWeight = FontWeight.Bold)
            )
        }

        Text(
            text = selectedItem,
            modifier = Modifier
                .wrapContentSize()
                .clickable { expanded = clickableText }
        )
    }
}