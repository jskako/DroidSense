package ui.composable.elements

import adb.DeviceOptions
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.Icon
import androidx.compose.material.Text
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


@OptIn(ExperimentalMaterialApi::class)
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
                modifier = Modifier.clickable {
                    isMenuVisible = true
                }
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
                        enabled = it.enabled
                    ) {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = it.text,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}