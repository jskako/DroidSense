package ui.composable.elements

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun BasicTextCaption(
    text1: String,
    text2: String,
    specialChar: String = ":",
    color: Color = Color.Black,
    fontSize: TextUnit = 16.sp
) {
    Row {
        BasicTextField(
            value = "$text1$specialChar",
            onValueChange = { },
            textStyle = TextStyle(
                color = color,
                fontSize = fontSize,
                fontWeight = FontWeight.Bold
            )
        )
        Spacer(modifier = Modifier.width(8.dp))
        BasicTextField(
            value = text2,
            onValueChange = { },
            textStyle = TextStyle(
                color = color,
                fontSize = fontSize,
            )
        )
    }
}