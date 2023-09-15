package ui

import androidx.compose.ui.window.application
import ui.application.ApplicationEvent
import ui.application.ApplicationManager

fun main() = application {
    ApplicationManager().CreateWindow(
        ApplicationEvent.NewWindow
    )
}