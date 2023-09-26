package ui.application

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector

data class WindowState(
    val title: String,
    val icon: ImageVector,
    val content: @Composable (() -> Unit)? = null,
    val openNewWindow: (String, ImageVector, @Composable () -> Unit) -> Unit,
    val exit: () -> Unit,
    private val close: (WindowState) -> Unit
) {
    fun close() = close(this)
}