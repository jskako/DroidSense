package ui.composable.sections.log

import adb.log.SelectOption
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowOutward
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Deselect
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.MoveDown
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.SelectAll
import androidx.compose.material.icons.filled.TextDecrease
import androidx.compose.material.icons.filled.TextIncrease
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import ui.composable.elements.iconButtons.TooltipIconButton
import utils.Colors.darkBlue
import utils.Colors.lightGray
import utils.getStringResource

@Composable
fun MainButtonsSection(
    onClearLogs: () -> Unit,
    isClearLogsEnabled: Boolean,
    onExportLogs: () -> Unit,
    isRunning: Boolean,
    scrollToEnd: Boolean,
    onScrollToEnd: (Boolean) -> Unit,
    scrollToEndEnabled: Boolean,
    reverseLogs: Boolean,
    onReverseLogs: (Boolean) -> Unit,
    onFontSize: (FontSize) -> Unit,
    saveToDatabase: Boolean,
    onSaveToDatabase: (Boolean) -> Unit,
    isExportEnabled: Boolean,
    isSelectEnabled: Boolean,
    onSelect: (SelectOption) -> Unit
) {

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TooltipIconButton(
            icon = Icons.Default.Delete,
            tooltip = getStringResource("info.clear.logs"),
            function = onClearLogs,
            tint = if (isClearLogsEnabled) darkBlue else lightGray,
            isEnabled = isClearLogsEnabled
        )
        TooltipIconButton(
            modifier = Modifier
                .background(
                    color = if (scrollToEnd && scrollToEndEnabled) darkBlue else Color.Transparent,
                    shape = CircleShape
                ),
            icon = Icons.Default.MoveDown,
            tooltip = getStringResource("info.scroll.end"),
            isEnabled = scrollToEndEnabled,
            tint = when {
                !scrollToEndEnabled -> lightGray
                scrollToEnd -> Color.White
                else -> darkBlue
            },
            function = { onScrollToEnd(!scrollToEnd) }
        )
        TooltipIconButton(
            modifier = Modifier
                .background(
                    color = if (reverseLogs) darkBlue else Color.Transparent,
                    shape = CircleShape
                ),
            icon = Icons.Default.ArrowOutward,
            tooltip = getStringResource("info.reversed"),
            tint = if (reverseLogs) Color.White else darkBlue,
            function = { onReverseLogs(!reverseLogs) }
        )
        TooltipIconButton(
            icon = Icons.Default.TextIncrease,
            tooltip = getStringResource("info.font.size.increase"),
            function = { onFontSize(FontSize.INCREASE) }
        )
        TooltipIconButton(
            icon = Icons.Default.TextDecrease,
            tooltip = getStringResource("info.font.size.decrease"),
            function = { onFontSize(FontSize.DECREASE) }
        )
        TooltipIconButton(
            modifier = Modifier
                .background(
                    color = Color.Transparent,
                    shape = CircleShape
                ),
            icon = Icons.Default.Save,
            tooltip = getStringResource("info.save.database"),
            tint = when {
                !isRunning && saveToDatabase -> darkBlue
                else -> lightGray
            },
            function = {
                if (!isRunning) {
                    onSaveToDatabase(!saveToDatabase)
                }
            }
        )
        TooltipIconButton(
            isEnabled = isExportEnabled,
            tint = if (isExportEnabled) darkBlue else lightGray,
            icon = Icons.Default.FileDownload,
            tooltip = getStringResource("info.export.logs"),
            function = onExportLogs
        )
        TooltipIconButton(
            isEnabled = isSelectEnabled,
            tint = if (isSelectEnabled) darkBlue else lightGray,
            icon = Icons.Default.SelectAll,
            tooltip = getStringResource("info.select.logs"),
            function = {
                onSelect(SelectOption.SELECT)
            }
        )
        TooltipIconButton(
            isEnabled = isSelectEnabled,
            tint = if (isSelectEnabled) darkBlue else lightGray,
            icon = Icons.Default.Deselect,
            tooltip = getStringResource("info.deselect.logs"),
            function = {
                onSelect(SelectOption.DESELECT)
            }
        )
    }
}