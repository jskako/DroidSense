package ui.composable.elements

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

@Composable
fun CustomText(
    text: String,
    fontSize: TextUnit = 15.sp,
    isBold: Boolean = false,
) {
    Text(
        modifier = Modifier.fillMaxWidth(),
        text = text,
        style = TextStyle(
            color = Color.Black,
            fontSize = fontSize,
            fontWeight = if (isBold) FontWeight.Bold else FontWeight.Normal
        )
    )
}