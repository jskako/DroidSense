package ui.composable.sections.log

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowOutward
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MoveDown
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.TextDecrease
import androidx.compose.material.icons.filled.TextIncrease
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import ui.composable.elements.iconButtons.IconButtonsColumn
import ui.composable.elements.iconButtons.IconButtonsData
import utils.Colors.darkBlue
import utils.Colors.darkRed
import utils.getStringResource

@Composable
fun MainButtonsSection(
    onClearLogs: () -> Unit,
    scrollToEnd: Boolean,
    onScrollToEnd: (Boolean) -> Unit,
    reverseLogs: Boolean,
    onReverseLogs: (Boolean) -> Unit,
    onFontSize: (FontSize) -> Unit,
    saveToDatabase: Boolean,
    onSaveToDatabase: (Boolean) -> Unit
) {
    val iconSize by remember {
        mutableStateOf(36.dp)
    }

    IconButtonsColumn(
        listOf(
            IconButtonsData(
                modifier = Modifier.size(iconSize),
                icon = Icons.Default.Delete,
                contentDescription = getStringResource("info.clear.logs"),
                function = onClearLogs
            ),
            IconButtonsData(
                modifier = Modifier
                    .background(
                        color = if (scrollToEnd) darkBlue else Color.Transparent,
                        shape = CircleShape
                    )
                    .size(iconSize),
                icon = Icons.Default.MoveDown,
                contentDescription = getStringResource("info.scroll.end"),
                tint = if (scrollToEnd) Color.White else darkBlue,
                function = { onScrollToEnd(!scrollToEnd) }
            ),
            IconButtonsData(
                modifier = Modifier
                    .background(
                        color = if (reverseLogs) darkBlue else Color.Transparent,
                        shape = CircleShape
                    )
                    .size(iconSize),
                icon = Icons.Default.ArrowOutward,
                contentDescription = getStringResource("info.reversed"),
                tint = if (reverseLogs) Color.White else darkBlue,
                function = { onReverseLogs(!reverseLogs) }
            ),
            IconButtonsData(
                modifier = Modifier.size(iconSize),
                icon = Icons.Default.TextIncrease,
                contentDescription = getStringResource("info.font.size.increase"),
                function = { onFontSize(FontSize.INCREASE) }
            ),
            IconButtonsData(
                modifier = Modifier.size(iconSize),
                icon = Icons.Default.TextDecrease,
                contentDescription = getStringResource("info.font.size.decrease"),
                function = { onFontSize(FontSize.DECREASE) }
            ),
            IconButtonsData(
                modifier = Modifier
                    .background(
                        color = if (saveToDatabase) darkRed else Color.Transparent,
                        shape = CircleShape
                    )
                    .size(iconSize),
                icon = Icons.Default.Save,
                contentDescription = getStringResource("info.save.database"),
                tint = if (saveToDatabase) Color.White else darkRed,
                function = { onSaveToDatabase(!saveToDatabase) }
            ),
        )
    )
}