package ui.composable.elements.window

import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import ui.application.ApplicationEvent
import ui.application.ApplicationState
import ui.application.WindowStateManager.setWindowState

@Composable
fun Window(event: ApplicationEvent) {
    val applicationState = remember { ApplicationState() }

    for (window in applicationState.windows) {
        key(window) {
            when (event) {
                ApplicationEvent.NavWindow -> {
                    window.also {
                        setWindowState(it)
                        NavWindow(
                            state = it,
                        )
                    }
                }
            }
        }
    }
}