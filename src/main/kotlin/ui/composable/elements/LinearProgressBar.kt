package ui.composable.elements

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import utils.Colors.darkBlue

@Composable
fun LinearProgressBar(
    text: String,
    color: Color = darkBlue,
    isVisible: Boolean
) {
    if (isVisible) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = text,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 4.dp, bottom = 8.dp)
            )
            LinearProgressIndicator(
                color = color,
            )
        }
    }
}