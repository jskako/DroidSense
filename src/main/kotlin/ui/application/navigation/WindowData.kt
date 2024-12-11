package ui.application.navigation

import androidx.compose.ui.graphics.vector.ImageVector
import data.ArgsText
import ui.application.WindowExtra

data class WindowData(
    val title: ArgsText,
    val icon: ImageVector?,
    val windowExtra: WindowExtra
)