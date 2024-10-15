package ui.composable.elements

import androidx.compose.foundation.text.BasicTextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

@Composable
fun BasicText(
    modifier: Modifier = Modifier,
    value: String,
    color: Color = Color.Black,
    fontSize: TextUnit = 16.sp,
    readOnly: Boolean = true,
    isBold: Boolean = false
) {
    val fontWeight = if (isBold) FontWeight.Bold else FontWeight.Normal
    BasicTextField(
        modifier = modifier,
        value = value,
        readOnly = readOnly,
        onValueChange = { },
        textStyle = TextStyle(
            color = color,
            fontSize = fontSize,
            fontWeight = fontWeight
        )
    )
}