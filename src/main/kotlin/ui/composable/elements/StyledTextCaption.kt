package ui.composable.elements

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import utils.Colors.darkBlue

@Composable
fun StyledTextCaption(
    text1: String,
    text2: String,
    specialChar: String = ":",
    color: Color = Color.Black,
    fontSize: TextUnit = 16.sp
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = "$text1$specialChar",
            style = TextStyle(
                color = color,
                fontSize = fontSize,
                fontWeight = FontWeight.Bold
            ),
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = text2,
            style = TextStyle(
                color = color,
                fontSize = fontSize,
            )
        )
    }
}