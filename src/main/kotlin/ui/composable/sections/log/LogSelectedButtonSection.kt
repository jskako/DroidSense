package ui.composable.sections.log

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.QuestionAnswer
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import ui.composable.elements.iconButtons.TooltipIconButton
import utils.Colors.darkBlue
import utils.Colors.darkRed
import utils.Colors.lightGray
import utils.getStringResource

@Composable
fun LogSelectedButtonSection(
    selectedLogsSize: Int,
    onExportLogs: () -> Unit,
    onCopyLogs: () -> Unit,
    isExportEnabled: Boolean
) {

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TooltipIconButton(
            isEnabled = false,
            tint = darkRed,
            text = selectedLogsSize.toString(),
            tooltip = getStringResource("info.selected.logs.number"),
            function = {}
        )
        TooltipIconButton(
            isEnabled = isExportEnabled,
            tint = if (isExportEnabled) darkBlue else lightGray,
            icon = Icons.Default.FileDownload,
            tooltip = getStringResource("info.export.selected.logs"),
            function = onExportLogs
        )
        TooltipIconButton(
            tint = darkBlue,
            icon = Icons.Default.ContentCopy,
            tooltip = getStringResource("info.copy.selected.logs"),
            function = onCopyLogs
        )
        TooltipIconButton(
            isEnabled = false,
            tint = lightGray,
            icon = Icons.Default.QuestionAnswer,
            tooltip = getStringResource("info.ask.ai"),
            function = {}
        )
    }
}