package ui.composable.elements.log

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import log.LogData
import ui.composable.elements.BasicText
import ui.composable.elements.TextBox

@Composable
fun LogCard(item: LogData) {

    val (backgroundColor, textColor) = getColors(item.level.simplified())

    Row {
        TextBox(
            text = item.time,
            width = 100.dp,
            isBold = false,
            startPadding = 0.dp,
            endPadding = 0.dp
        )
        Spacer(modifier = Modifier.width(8.dp))
        TextBox(
            text = item.level.simplified().toString(),
            backgroundColor = backgroundColor,
            textColor = textColor
        )
        Spacer(modifier = Modifier.width(8.dp))
        BasicText(
            value = item.log,
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