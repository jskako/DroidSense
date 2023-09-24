package ui

import androidx.compose.ui.window.application
import ui.application.ApplicationEvent
import ui.application.ApplicationManager.CreateWindow

fun main() = application {
    CreateWindow(
        event = ApplicationEvent.NewWindow
    )
}