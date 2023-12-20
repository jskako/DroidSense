package ui.composable.elements

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import utils.Colors.darkBlue
import utils.getStringResource

@Composable
fun DropdownItem(
    list: List<String>,
    text: String,
    onItemSelected: (String) -> Unit,
    enabled: Boolean = true,
    buttonText: String,
    showPackageName: Boolean = false,
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedItem by remember { mutableStateOf(text) }
    var searchText by remember { mutableStateOf("") }

    val color = if (enabled) darkBlue else Color.Gray

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Column {
            OutlinedButton(
                text = buttonText,
                onClick = { expanded = enabled },
                modifier = Modifier.wrapContentSize(),
                color = color
            )

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                TextField(
                    value = searchText,
                    onValueChange = {
                        searchText = it
                    },
                    placeholder = { Text(getStringResource("info.search")) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                )

                list.filter { it.contains(searchText, ignoreCase = true) }
                    .forEach { property ->
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

        if(showPackageName) {
            Text(
                text = selectedItem,
                modifier = Modifier
                    .wrapContentSize()
            )
        }
    }
}
