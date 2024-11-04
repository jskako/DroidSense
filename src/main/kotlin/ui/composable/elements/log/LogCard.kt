package ui.composable.elements.log

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import data.model.items.LogItem
import ui.composable.elements.BasicText
import ui.composable.elements.TextBox
import utils.Colors.lightGray

@Composable
fun LogCard(
    item: LogItem,
    fontSize: TextUnit,
    onClicked: () -> Unit
) {

    val (levelBackgroundColor, levelTextColor) = item.level.getLogColor()

    Column(
        modifier = Modifier
            .background(color = if (item.isSelected) lightGray else Color.Transparent)
            .clickable { onClicked() }
    ) {
        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextBox(
                startPadding = 0.dp,
                text = item.time,
                backgroundColor = Color.Transparent,
                fontSize = fontSize,
            )
            Spacer(modifier = Modifier.width(8.dp))
            TextBox(
                startPadding = 0.dp,
                text = item.level.toString(),
                fontSize = fontSize,
                backgroundColor = levelBackgroundColor,
                textColor = levelTextColor,
            )
        }
        BasicText(
            value = item.text,
            fontSize = fontSize,
            color = if (item.level.simplified() == 'E') Color.Red else Color.Black
        )
        HorizontalDivider(
            modifier = Modifier
                .padding(top = 3.dp, bottom = 3.dp)
        )
    }
}