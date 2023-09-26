package ui.composable.elements

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import utils.Colors.darkBlue

@Composable
fun OutlinedButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
        .fillMaxWidth()
        .height(48.dp)
) {
    Button(
        onClick = {
            onClick()
        },
        modifier = modifier,
        border = BorderStroke(1.dp, darkBlue),
        colors = ButtonDefaults.outlinedButtonColors(contentColor = darkBlue)
    ) {
        Text(text = text, color = darkBlue)
    }
}