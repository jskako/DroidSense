package ui.composable.elements.log

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import log.LogData
import utils.getTimeStamp

@Composable
fun LogCard(item: LogData) {

    val (backgroundColor, textColor) = getColors(item.level.simplified())

    Row {
        Text(text = getTimeStamp())
        Spacer(modifier = Modifier.width(8.dp))
        Box(
            modifier = Modifier
                .width(25.dp)
                .background(color = backgroundColor)
                .padding(start = 3.dp, end = 3.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = item.level.simplified().toString(),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                color = textColor,
                style = TextStyle(fontWeight = FontWeight.Bold)
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = item.log,
            color = if (item.level.simplified() == 'E') Color.Red else Color.Black
        )
    }
}

private fun getColors(logType: Char): Pair<Color, Color> {
    return when (logType) {
        'I' -> Color.Green to Color.White
        'E' -> Color.Red to Color.Black
        'W' -> Color.Yellow to Color.Black
        'V' -> Color.White to Color.Black
        'D' -> Color.Blue to Color.White
        else -> Color.White to Color.Black
    }
}