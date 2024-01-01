package ui.application

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.graphics.vector.ImageVector
import utils.getStringResource

data class ApplicationState(
    val title: String = getStringResource("app.name"),
    val icon: ImageVector? = null
) {

    val windows = mutableStateListOf<WindowState>()

    init {
        windows += createWindowState(title, icon, WindowExtra())
    }

    fun openNewWindow(title: String, icon: ImageVector?, extra: WindowExtra) {
        windows += createWindowState(title, icon, extra)
    }

    private fun exit() {
        windows.clear()
    }

    private fun createWindowState(
        title: String,
        icon: ImageVector?,
        extra: WindowExtra
    ): WindowState {
        return WindowState(
            title = title,
            icon = icon,
            extra = extra,
            openNewWindow = ::openNewWindow,
            exit = ::exit,
            close = { window ->
                extra.onClose?.let {
                    it()
                }
                windows.remove(window)
            }
        )
    }
}