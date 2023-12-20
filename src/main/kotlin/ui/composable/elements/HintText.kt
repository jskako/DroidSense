package ui.composable.elements

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import utils.Colors.darkBlue
import utils.getStringResource

@Composable
internal fun HintText(
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

@Composable
private fun HintText(text: String) {
    Box(
        modifier = Modifier
            .padding(8.dp)
            .alpha(0.5f)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.body2,
            color = Color.Gray
        )
    }
}