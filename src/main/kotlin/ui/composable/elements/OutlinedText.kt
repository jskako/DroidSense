package ui.composable.elements

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun OutlinedText(
    modifier: Modifier = Modifier,
    text: String,
    hintText: StringResource,
    readOnly: Boolean = false,
    enabled: Boolean = true,
    onValueChanged: (String) -> Unit,
    trailingIcon: @Composable (() -> Unit)? = null
) {
    OutlinedTextField(
        modifier = modifier,
        value = text,
        readOnly = readOnly,
        enabled = enabled,
        onValueChange = {
            onValueChanged(it)
        },
        trailingIcon = trailingIcon,
        label = { HintText(stringResource(hintText)) },
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Done
        )
    )
}