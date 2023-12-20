package ui.composable.elements

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
internal fun TextBox(
    text: String,
    textColor: Color = Color.Black,
    backgroundColor: Color = Color.White,
    width: Dp? = null,
    startPadding: Dp = 3.dp,
    endPadding: Dp = 3.dp,
    isBold: Boolean = true,
    cornerRadius: Dp = 0.dp
) {
    Box(
        modifier = Modifier
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(cornerRadius)
            )
            .let {
                if (width != null) it.width(width) else it
            }
            .padding(start = startPadding, end = endPadding),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            textAlign = TextAlign.Center,
            color = textColor,
            style = if (isBold) {
                TextStyle(fontWeight = FontWeight.Bold)
            } else {
                TextStyle.Default
            }
        )
    }
}