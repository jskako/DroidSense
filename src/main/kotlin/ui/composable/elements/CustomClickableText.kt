package ui.composable.elements

import androidx.compose.foundation.clickable
import androidx.compose.foundation.text.ClickableText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Composable
fun CustomClickableText(
    text: String,
    function: () -> Unit
) {
    val textStyle = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp
    )

    ClickableText(
        text = AnnotatedString(text),
        style = textStyle,
        onClick = {
            function()
        },
        modifier = Modifier
            .clickable { }
    )
}