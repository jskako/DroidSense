package ui.composable.elements

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.jskako.droidsense.generated.resources.Res
import com.jskako.droidsense.generated.resources.info_add
import org.jetbrains.compose.resources.StringResource
import ui.composable.elements.iconButtons.TooltipIconButton
import utils.Colors.darkBlue
import utils.Colors.lightGray

@Composable
fun AddRow(
    modifier: Modifier = Modifier,
    hintText: StringResource,
    icon: ImageVector = Icons.Default.Add,
    tooltipText: StringResource = Res.string.info_add,
    enabled: Boolean = true,
    onClick: (String) -> Unit,
    additionalText: String? = null,
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
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                additionalText?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )
                }
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
        }
    )
}