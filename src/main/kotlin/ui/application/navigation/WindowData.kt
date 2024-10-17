package ui.application.navigation

import androidx.compose.ui.graphics.vector.ImageVector
import ui.application.WindowExtra

data class WindowData(
    val title: String,
    val icon: ImageVector?,
    val windowExtra: WindowExtra
)