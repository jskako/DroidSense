package ui.application

import androidx.compose.runtime.mutableStateListOf
import ui.application.navigation.WindowData
import utils.getStringResource

class ApplicationState {

    val windows = mutableStateListOf<WindowState>()

    init {
        windows += createWindowState(
            WindowData(
                title = getStringResource("app.name"),
                icon = null,
                windowExtra = WindowExtra()
            )
        )
    }

    fun openNewWindow(windowData: WindowData) {
        windows += createWindowState(
            windowData = windowData
        )
    }

    private fun exit() {
        windows.clear()
    }

    private fun createWindowState(
        windowData: WindowData
    ): WindowState {
        return WindowState(
            title = windowData.title,
            icon = windowData.icon,
            extra = windowData.windowExtra,
            openNewWindow = ::openNewWindow,
            exit = ::exit,
            close = { window ->
                windowData.windowExtra.onClose?.let {
                    it()
                }
                windows.remove(window)
            }
        )
    }
}