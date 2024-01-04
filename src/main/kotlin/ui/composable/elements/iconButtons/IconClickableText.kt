package ui.composable.elements.iconButtons

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import utils.Colors.darkBlue
import utils.EMPTY_STRING

@Composable
fun IconClickableText(
    icon: ImageVector,
    iconColor: Color = darkBlue,
    text: String,
    contentDescription: String = EMPTY_STRING,
    function: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = iconColor
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            modifier = Modifier.clickable {
                function()
            },
            textAlign = TextAlign.Center,
            text = text
        )
    }
}
