package ui.composable.elements

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import utils.Colors.darkBlue
import utils.getStringResource

@Composable
internal fun HintBox(
    modifier: Modifier = Modifier,
    text: String,
    icon: ImageVector = Icons.Default.Search,
    hintText: String = getStringResource("info.search"),
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
        leadingIcon = {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = darkBlue
            )
        },
        modifier = modifier
    )
}