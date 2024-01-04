package ui.composable.elements

import adb.DeviceOptions
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClickableMenu(
    text: String,
    functions: List<DeviceOptions>
) {
    var isMenuVisible by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = isMenuVisible,
        onExpandedChange = { isMenuVisible = !isMenuVisible },
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            OutlinedButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .menuAnchor(),
                onClick = { isMenuVisible = true },
                text = text
            )
            DropdownMenu(
                modifier = Modifier.exposedDropdownSize(),
                expanded = isMenuVisible,
                onDismissRequest = { isMenuVisible = false }
            ) {
                functions.forEach {
                    DropdownMenuItem(
                        onClick = {
                            it.function()
                            isMenuVisible = false
                        },
                        text = {
                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                text = it.text,
                                textAlign = TextAlign.Center
                            )
                        }
                    )
                }
            }
        }
    }
}