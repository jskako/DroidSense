package ui.composable.sections

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import notifications.LogManager.clearLogs
import notifications.LogManager.logs
import ui.composable.elements.ScopedButton
import ui.composable.elements.log.LogsView
import utils.copyToClipboard
import utils.exportToFile
import utils.getStringResource
import utils.prepareLogs

@Composable
fun LogSection() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.surface)
            .background(Color.White)
            .padding(16.dp)
    ) {
        Row {
            ScopedButton(
                startPadding = 25.dp,
                endPadding = 0.dp,
                title = getStringResource("info.log.section.copy"),
                function = {
                    logs.prepareLogs().copyToClipboard()
                }
            )

            ScopedButton(
                startPadding = 10.dp,
                endPadding = 0.dp,
                title = getStringResource("info.log.section.export"),
                function = {
                    logs.reversed().prepareLogs().exportToFile()
                }
            )

            ScopedButton(
                startPadding = 10.dp,
                endPadding = 20.dp,
                title = getStringResource("info.log.section.clear"),
                function = {
                    clearLogs()
                }
            )
        }
        LogsView()
    }
}