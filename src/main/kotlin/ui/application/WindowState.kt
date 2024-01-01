package ui.application

import androidx.compose.ui.graphics.vector.ImageVector

data class WindowState(
    val title: String,
    val icon: ImageVector?,
    val extra: WindowExtra,
    val openNewWindow: (String, ImageVector?, WindowExtra) -> Unit,
    val exit: () -> Unit,
    private val close: (WindowState) -> Unit
) {
    fun close() = close(this)
}