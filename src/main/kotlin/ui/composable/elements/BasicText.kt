package ui.composable.elements

import androidx.compose.foundation.text.BasicTextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

@Composable
fun BasicText(
    value: String,
    color: Color = Color.Black,
    fontSize: TextUnit = 16.sp
) {
    BasicTextField(
        value = value,
        onValueChange = { },
        textStyle = TextStyle(
            color = color,
            fontSize = fontSize
        )
    )
}