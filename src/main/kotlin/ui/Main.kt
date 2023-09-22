package ui

import androidx.compose.ui.window.application
import ui.application.ApplicationEvent
import ui.application.ApplicationManager
import ui.composable.screens.RequirementsScreen

fun main() = application {
    ApplicationManager().CreateWindow(
        event = ApplicationEvent.NewWindow { RequirementsScreen() }
    )
}