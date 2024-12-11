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
import com.jskako.droidsense.generated.resources.Res
import com.jskako.droidsense.generated.resources.info_clear_logs
import com.jskako.droidsense.generated.resources.info_deselect_logs
import com.jskako.droidsense.generated.resources.info_export_logs
import com.jskako.droidsense.generated.resources.info_font_size_decrease
import com.jskako.droidsense.generated.resources.info_font_size_increase
import com.jskako.droidsense.generated.resources.info_reversed
import com.jskako.droidsense.generated.resources.info_save_database
import com.jskako.droidsense.generated.resources.info_scroll_end
import com.jskako.droidsense.generated.resources.info_select_logs
import ui.composable.elements.iconButtons.TooltipIconButton
import utils.Colors.darkBlue
import utils.Colors.lightGray

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
            tooltip = Res.string.info_clear_logs,
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
            tooltip = Res.string.info_scroll_end,
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
            tooltip = Res.string.info_reversed,
            tint = if (reverseLogs) Color.White else darkBlue,
            function = { onReverseLogs(!reverseLogs) }
        )
        TooltipIconButton(
            icon = Icons.Default.TextIncrease,
            tooltip = Res.string.info_font_size_increase,
            function = { onFontSize(FontSize.INCREASE) }
        )
        TooltipIconButton(
            icon = Icons.Default.TextDecrease,
            tooltip = Res.string.info_font_size_decrease,
            function = { onFontSize(FontSize.DECREASE) }
        )
        TooltipIconButton(
            modifier = Modifier
                .background(
                    color = Color.Transparent,
                    shape = CircleShape
                ),
            icon = Icons.Default.Save,
            tooltip = Res.string.info_save_database,
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
            tooltip = Res.string.info_export_logs,
            function = onExportLogs
        )
        TooltipIconButton(
            isEnabled = isSelectEnabled,
            tint = if (isSelectEnabled) darkBlue else lightGray,
            icon = Icons.Default.SelectAll,
            tooltip = Res.string.info_select_logs,
            function = {
                onSelect(SelectOption.SELECT)
            }
        )
        TooltipIconButton(
            isEnabled = isSelectEnabled,
            tint = if (isSelectEnabled) darkBlue else lightGray,
            icon = Icons.Default.Deselect,
            tooltip = Res.string.info_deselect_logs,
            function = {
                onSelect(SelectOption.DESELECT)
            }
        )
    }
}