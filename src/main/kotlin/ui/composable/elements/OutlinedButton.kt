package ui.composable.elements

import androidx.compose.foundation.BorderStroke
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import utils.Colors.darkBlue
import utils.Colors.lightGray

@Composable
fun OutlinedButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    containerColor: Color = Color.Unspecified,
    contentColor: Color = darkBlue,
    disabledContainerColor: Color = lightGray,
    disabledContentColor: Color = Color.Unspecified
) {
    Button(
        onClick = {
            onClick()
        },
        enabled = enabled,
        modifier = modifier,
        border = BorderStroke(
            width = 1.dp,
            color = if (enabled) contentColor else Color.Unspecified
        ),
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = contentColor,
            containerColor = containerColor,
            disabledContainerColor = disabledContainerColor,
            disabledContentColor = disabledContentColor
        )
    ) {
        Text(
            text = text,
            color = if (enabled) contentColor else Color.Unspecified
        )
    }
}