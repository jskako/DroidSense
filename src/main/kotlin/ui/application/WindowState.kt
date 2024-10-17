package ui.application

import androidx.compose.ui.graphics.vector.ImageVector
import ui.application.navigation.WindowData

data class WindowState(
    val title: String,
    val icon: ImageVector?,
    val extra: WindowExtra,
    val openNewWindow: (WindowData) -> Unit,
    val exit: () -> Unit,
    private val close: (WindowState) -> Unit
) {
    fun close() = close(this)
}