package ui.composable.elements

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FindInPage
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import ui.composable.elements.iconButtons.TooltipIconButton
import utils.Colors.darkBlue
import utils.pickFile

@Composable
internal fun SelectableText(
    modifier: Modifier = Modifier,
    text: String,
    infoText: String,
    hintText: String,
    onValueChanged: (String) -> Unit
) {
    OutlinedTextField(
        value = text,
        onValueChange = {
            onValueChanged(it)
        },
        label = { HintText(hintText) },
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Done
        ),
        trailingIcon = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    modifier = Modifier.clickable {
                        runCatching {
                            onValueChanged(pickFile()?.absolutePath ?: "")
                        }.getOrDefault(text)

                    },
                    imageVector = Icons.Default.FindInPage,
                    contentDescription = null,
                    tint = darkBlue
                )

                TooltipIconButton(
                    modifier = Modifier.padding(start = 4.dp),
                    icon = Icons.Default.Info,
                    tooltip = infoText,
                    function = {},
                    plainTooltipPositionProvider = 40.dp
                )
            }
        },
        modifier = modifier
    )
}