package ui.composable.elements

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import ui.composable.elements.iconButtons.TooltipIconButton
import utils.Colors.darkBlue
import utils.Colors.lightGray
import utils.getStringResource

@Composable
fun AddRow(
    modifier: Modifier = Modifier,
    hintText: String,
    icon: ImageVector = Icons.Default.Add,
    tooltipText: String = getStringResource("info.add"),
    enabled: Boolean = true,
    onClick: (String) -> Unit
) {

    var input by remember { mutableStateOf("") }

    OutlinedText(
        modifier = modifier,
        text = input,
        enabled = enabled,
        hintText = hintText,
        onValueChanged = { changedValue ->
            input = changedValue
        },
        trailingIcon = {
            TooltipIconButton(
                modifier = Modifier.padding(end = 8.dp),
                isEnabled = enabled,
                tint = if (enabled) darkBlue else lightGray,
                icon = icon,
                tooltip = tooltipText,
                function = {
                    onClick(input)
                    input = ""
                }
            )
        }
    )
}