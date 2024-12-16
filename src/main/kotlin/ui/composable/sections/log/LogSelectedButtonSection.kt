package ui.composable.sections.log

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.QuestionAnswer
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.jskako.droidsense.generated.resources.Res
import com.jskako.droidsense.generated.resources.info_ask_ai
import com.jskako.droidsense.generated.resources.info_copy_selected_logs
import com.jskako.droidsense.generated.resources.info_export_selected_logs
import ui.composable.elements.iconButtons.TooltipIconButton
import utils.Colors.darkBlue
import utils.Colors.lightGray

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
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            text = selectedLogsSize.toString(),
            color = lightGray,
            textAlign = TextAlign.Center,
        )
        TooltipIconButton(
            isEnabled = isExportEnabled,
            tint = if (isExportEnabled) darkBlue else lightGray,
            icon = Icons.Default.FileDownload,
            tooltip = Res.string.info_export_selected_logs,
            function = onExportLogs
        )
        TooltipIconButton(
            tint = darkBlue,
            icon = Icons.Default.ContentCopy,
            tooltip = Res.string.info_copy_selected_logs,
            function = onCopyLogs
        )
        TooltipIconButton(
            isEnabled = false,
            tint = lightGray,
            icon = Icons.Default.QuestionAnswer,
            tooltip = Res.string.info_ask_ai,
            function = {}
        )
    }
}