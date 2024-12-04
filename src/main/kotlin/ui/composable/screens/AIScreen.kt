package ui.composable.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import notifications.InfoManager
import ui.application.WindowStateManager
import ui.composable.elements.window.Sources
import ui.composable.sections.ai.AISection
import ui.composable.sections.info.InfoSection

@Composable
fun AIScreen(
    windowStateManager: WindowStateManager,
    sources: Sources
) {

    val infoManager = remember { InfoManager() }
    val scope = rememberCoroutineScope()

    Column {

        InfoSection(
            onCloseClicked = { infoManager.clearInfoMessage() },
            message = infoManager.infoManagerData.value.message,
            color = infoManager.infoManagerData.value.color
        )

        AISection(
            windowStateManager = windowStateManager,
            sources = sources,
            onMessage = {
                infoManager.showMessage(
                    infoManagerData = it,
                    scope = scope
                )
            }
        )
    }
}