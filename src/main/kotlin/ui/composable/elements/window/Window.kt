package ui.composable.elements.window

import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import ui.application.ApplicationEvent
import ui.application.ApplicationState
import ui.application.WindowStateManager

@Composable
fun Window(event: ApplicationEvent) {
    val applicationState = remember { ApplicationState() }
    val windowStateManager = remember { WindowStateManager() }

    for (window in applicationState.windows) {
        key(window) {
            when (event) {
                ApplicationEvent.NavWindow -> {
                    window.also {
                        windowStateManager.setWindowState(it)
                        NavWindow(
                            state = it,
                            windowStateManager = windowStateManager
                        )
                    }
                }
            }
        }
    }
}