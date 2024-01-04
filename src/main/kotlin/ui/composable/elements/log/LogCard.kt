package ui.composable.elements.log

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import log.LogData
import ui.composable.elements.BasicText
import ui.composable.elements.TextBox

@Composable
fun LogCard(
    item: LogData,
    fontSize: TextUnit
) {

    val (backgroundColor, textColor) = item.level.getLogColor()

    Column {
        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextBox(
                startPadding = 0.dp,
                text = item.time,
                fontSize = fontSize,
            )
            Spacer(modifier = Modifier.width(8.dp))
            TextBox(
                startPadding = 0.dp,
                text = item.level.toString(),
                fontSize = fontSize,
                backgroundColor = backgroundColor,
                textColor = textColor,
            )
        }
        BasicText(
            value = item.log,
            fontSize = fontSize,
            color = if (item.level.simplified() == 'E') Color.Red else Color.Black
        )
        Divider(
            modifier = Modifier
                .padding(top = 3.dp, bottom = 3.dp)
        )
    }
}