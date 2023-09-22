package ui.application

import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import ui.composable.elements.Window

class ApplicationManager {

    @Composable
    fun CreateWindow(event: ApplicationEvent) {
        val applicationState = remember { ApplicationState() }
        when (event) {
            is ApplicationEvent.NewWindow -> {
                for (window in applicationState.windows) {
                    key(window) {
                        Window(
                            state = window,
                            startingComposable = event.startingComposable
                        )
                    }
                }
            }
        }
    }
}