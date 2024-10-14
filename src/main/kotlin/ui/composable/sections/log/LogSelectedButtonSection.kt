package ui.composable.sections.log

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.QuestionAnswer
import androidx.compose.material.icons.filled.SaveAlt
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ui.composable.elements.iconButtons.TooltipIconButton
import utils.Colors.darkBlue
import utils.Colors.darkRed
import utils.Colors.lightGray
import utils.getStringResource

@Composable
fun LogSelectedButtonSection(
    selectedLogsSize: Int,
    onExportLogs: () -> Unit,
    isExportEnabled: Boolean
) {
    val iconSize = 36.dp

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TooltipIconButton(
            modifier = Modifier.size(iconSize),
            isEnabled = false,
            tint = darkRed,
            text = selectedLogsSize.toString(),
            tooltip = getStringResource("info.selected.logs.number"),
            function = {}
        )
        TooltipIconButton(
            modifier = Modifier.size(iconSize),
            isEnabled = isExportEnabled,
            tint = if (isExportEnabled) darkBlue else lightGray,
            icon = Icons.Default.SaveAlt,
            tooltip = getStringResource("info.export.selected.logs"),
            function = onExportLogs
        )
        TooltipIconButton(
            modifier = Modifier.size(iconSize),
            tint = darkBlue,
            icon = Icons.Default.ContentCopy,
            tooltip = getStringResource("info.copy.selected.logs"),
            function = {}
        )
        TooltipIconButton(
            modifier = Modifier.size(iconSize),
            isEnabled = false,
            tint = lightGray,
            icon = Icons.Default.QuestionAnswer,
            tooltip = getStringResource("info.ask.ai"),
            function = {}
        )
        // TODO - add select all and unselect all buttons
    }
}