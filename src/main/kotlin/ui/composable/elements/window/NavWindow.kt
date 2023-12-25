package ui.composable.elements.window

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.window.Window
import java.awt.Dimension
import ui.application.WindowState
import ui.application.navigation.NavRoute
import ui.application.navigation.NavigationManager.navRoute
import ui.composable.screens.MainScreen
import ui.composable.screens.RequirementsScreen
import ui.composable.utils.createMenu
import utils.MIN_WINDOW_HEIGHT
import utils.MIN_WINDOW_WIDTH

@Composable
fun NavWindow(
    state: WindowState,
) = Window(
    onCloseRequest = state::close,
    icon = rememberVectorPainter(state.icon),
    title = state.title
) {
    window.minimumSize = Dimension(MIN_WINDOW_WIDTH, MIN_WINDOW_HEIGHT)
    if (navRoute != NavRoute.RequirementsScreen) {
        createMenu(state)
    }
    MaterialTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize()
        ) {
            state.extra.screen?.invoke() ?: when (navRoute) {
                is NavRoute.MainScreen -> {
                    MainScreen()
                }

                is NavRoute.RequirementsScreen -> {
                    RequirementsScreen()
                }
            }
        }
    }
}