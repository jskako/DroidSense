package ui.application

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.graphics.vector.ImageVector
import utils.getStringResource

data class ApplicationState(
    val title: String = getStringResource("app.name"),
    val icon: ImageVector = Icons.Filled.Build
) {

    val windows = mutableStateListOf<WindowState>()

    init {
        windows += createWindowState(title, icon, null)
    }

    fun openNewWindow(title: String, icon: ImageVector, content: @Composable (() -> Unit)? = null) {
        windows += createWindowState(title, icon, content)
    }

    private fun exit() {
        windows.clear()
    }

    private fun createWindowState(
        title: String,
        icon: ImageVector,
        content: @Composable (() -> Unit)? = null
    ): WindowState {
        return WindowState(
            title = title,
            icon = icon,
            content = content,
            openNewWindow = ::openNewWindow,
            exit = ::exit,
            close = { window -> windows.remove(window) }
        )
    }
}