package ui.composable.sections.log

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowOutward
import androidx.compose.material.icons.filled.Backup
import androidx.compose.material.icons.filled.Dataset
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MoveDown
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.TextDecrease
import androidx.compose.material.icons.filled.TextIncrease
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import ui.composable.elements.iconButtons.TooltipIconButton
import utils.Colors.darkBlue
import utils.Colors.darkRed
import utils.Colors.lightGray
import utils.getStringResource

@Composable
fun MainButtonsSection(
    onClearLogs: () -> Unit,
    onExportLogs: () -> Unit,
    scrollToEnd: Boolean,
    onScrollToEnd: (Boolean) -> Unit,
    reverseLogs: Boolean,
    onReverseLogs: (Boolean) -> Unit,
    onFontSize: (FontSize) -> Unit,
    saveToDatabase: Boolean,
    onSaveToDatabase: (Boolean) -> Unit,
    isExportEnabled: Boolean
) {
    val iconSize = 36.dp

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TooltipIconButton(
            modifier = Modifier.size(iconSize),
            icon = Icons.Default.Delete,
            tooltip = getStringResource("info.clear.logs"),
            function = onClearLogs
        )
        TooltipIconButton(
            modifier = Modifier
                .background(
                    color = if (scrollToEnd) darkBlue else Color.Transparent,
                    shape = CircleShape
                )
                .size(iconSize),
            icon = Icons.Default.MoveDown,
            tooltip = getStringResource("info.scroll.end"),
            tint = if (scrollToEnd) Color.White else darkBlue,
            function = { onScrollToEnd(!scrollToEnd) }
        )
        TooltipIconButton(
            modifier = Modifier
                .background(
                    color = if (reverseLogs) darkBlue else Color.Transparent,
                    shape = CircleShape
                )
                .size(iconSize),
            icon = Icons.Default.ArrowOutward,
            tooltip = getStringResource("info.reversed"),
            tint = if (reverseLogs) Color.White else darkBlue,
            function = { onReverseLogs(!reverseLogs) }
        )
        TooltipIconButton(
            modifier = Modifier.size(iconSize),
            icon = Icons.Default.TextIncrease,
            tooltip = getStringResource("info.font.size.increase"),
            function = { onFontSize(FontSize.INCREASE) }
        )
        TooltipIconButton(
            modifier = Modifier.size(iconSize),
            icon = Icons.Default.TextDecrease,
            tooltip = getStringResource("info.font.size.decrease"),
            function = { onFontSize(FontSize.DECREASE) }
        )
        TooltipIconButton(
            modifier = Modifier
                .background(
                    color = if (saveToDatabase) darkRed else Color.Transparent,
                    shape = CircleShape
                )
                .size(iconSize),
            isEnabled = false,
            icon = Icons.Default.Backup,
            tooltip = getStringResource("info.save.database"),
            tint = lightGray,
            function = { onSaveToDatabase(!saveToDatabase) }
        )
        TooltipIconButton(
            modifier = Modifier.size(iconSize),
            isEnabled = isExportEnabled,
            tint = if (isExportEnabled) darkBlue else lightGray,
            icon = Icons.Default.Save,
            tooltip = getStringResource("info.export.logs"),
            function = onExportLogs
        )
    }
}