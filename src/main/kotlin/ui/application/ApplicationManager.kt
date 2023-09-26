package ui.application

import androidx.compose.ui.window.application
import ui.composable.elements.window.Window

object ApplicationManager {

    fun createWindow(event: ApplicationEvent) {
        application {
            Window(
                event = event
            )
        }
    }
}