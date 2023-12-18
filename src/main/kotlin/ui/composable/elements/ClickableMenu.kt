package ui.composable.elements

import adb.DeviceOptions
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun ClickableMenu(
    text: String,
    functions: List<DeviceOptions>
) {
    var isMenuVisible by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        OutlinedButton(
            modifier = Modifier.fillMaxWidth()
                .height(48.dp),
            onClick = { isMenuVisible = true },
            text = text
        )
        DropdownMenu(
            expanded = isMenuVisible,
            onDismissRequest = { isMenuVisible = false }
        ) {
            functions.forEach {
                DropdownMenuItem(
                    onClick = {
                        it.function()
                        isMenuVisible = false
                    }
                ) {
                    Text(text = it.text)
                }
            }
        }
    }
}