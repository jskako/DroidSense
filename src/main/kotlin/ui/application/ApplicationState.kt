package ui.application

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.graphics.vector.ImageVector
import utils.getStringResource

data class ApplicationState(
    val title: String = getStringResource("app.name"),
    val icon: ImageVector = Icons.Filled.Build
) {

    val windows = mutableStateListOf<WindowState>()

    init {
        windows += WindowState(
            title,
            icon
        )
    }

    private fun exit() {
        windows.clear()
    }

    private fun WindowState(
        title: String,
        icon: ImageVector
    ) = WindowState(
        title = title,
        icon = icon,
        exit = ::exit,
        close = windows::remove
    )
}