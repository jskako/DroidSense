package ui.composable.elements

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FindInPage
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.PlainTooltipBox
import androidx.compose.material3.PlainTooltipState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import utils.Colors.darkBlue
import utils.pickFile

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SelectableText(
    modifier: Modifier = Modifier,
    text: String,
    infoText: String,
    hintText: String,
    onValueChanged: (String) -> Unit
) {
    val tooltipState = remember { PlainTooltipState() }
    val scope = rememberCoroutineScope()

    OutlinedTextField(
        value = text,
        onValueChange = {
            onValueChanged(it)
        },
        label = { HintBox(hintText) },
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Done
        ),
        trailingIcon = {
            Row(
                modifier = Modifier.padding(4.dp)
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

                PlainTooltipBox(
                    modifier = Modifier.padding(start = 4.dp),
                    tooltip = {
                        Text(
                            text = infoText,
                            color = Color.White
                        )
                    },
                    tooltipState = tooltipState,
                    containerColor = darkBlue
                ) {
                    Icon(
                        modifier = Modifier.clickable {
                            scope.launch {
                                tooltipState.show()
                            }
                        },
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        tint = darkBlue
                    )
                }
            }
        },
        modifier = modifier
    )
}