package ui.composable.elements

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType

@Composable
internal fun OutlinedText(
    modifier: Modifier = Modifier,
    text: String,
    hintText: String,
    readOnly: Boolean = false,
    onValueChanged: (String) -> Unit
) {
    OutlinedTextField(
        value = text,
        readOnly = readOnly,
        onValueChange = {
            onValueChanged(it)
        },
        label = { HintText(hintText) },
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Done
        ),
        modifier = modifier
    )
}