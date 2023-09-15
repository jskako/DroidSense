package ui.application

import androidx.compose.ui.graphics.vector.ImageVector

data class WindowState(
    val title: String,
    val icon: ImageVector,
    val exit: () -> Unit,
    private val close: (WindowState) -> Unit
) {
    fun close() = close(this)
}