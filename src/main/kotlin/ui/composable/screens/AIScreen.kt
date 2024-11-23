package ui.composable.screens

import androidx.compose.runtime.Composable
import ui.application.WindowStateManager
import ui.composable.elements.window.Sources
import ui.composable.sections.ai.AISection

@Composable
fun AIScreen(
    windowStateManager: WindowStateManager,
    sources: Sources
) {

    AISection(
        windowStateManager = windowStateManager
    )
}