package ui.composable.elements

import adb.DeviceOptions
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import utils.Colors
import utils.EMPTY_STRING


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClickableIconMenu(
    icon: ImageVector,
    functions: List<DeviceOptions>
) {
    var isMenuVisible by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = isMenuVisible,
        onExpandedChange = { isMenuVisible = !isMenuVisible }
    ) {
        Column {
            Icon(
                imageVector = icon,
                contentDescription = EMPTY_STRING,
                tint = Colors.darkBlue,
                modifier = Modifier
                    .clickable {
                        isMenuVisible = true
                    }
                    .menuAnchor()
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
                        },
                        enabled = it.enabled,
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