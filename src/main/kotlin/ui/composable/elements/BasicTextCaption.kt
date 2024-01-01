package ui.composable.elements

import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

@Composable
fun BasicTextCaption(
    text1: String,
    text2: String,
    specialChar: String = ":",
    color: Color = Color.Black,
    fontSize: TextUnit = 16.sp
) {
    val text = buildAnnotatedString {
        withStyle(
            style = SpanStyle(
                color = color,
                fontSize = fontSize,
                fontWeight = FontWeight.Bold
            )
        ) {
            append("${text1.trim()}$specialChar")
        }
        append(" ${text2.trim()}")
    }
    SelectionContainer {
        Text(text = text)
    }
}