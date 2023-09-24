package ui.composable.elements

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.window.MenuBar
import androidx.compose.ui.window.Window
import ui.application.WindowState
import ui.application.navigation.NavigationManager.currentRoute
import ui.application.navigation.Route
import ui.composable.screens.MainScreen
import ui.composable.screens.RequirementsScreen
import utils.getStringResource

@Composable
fun Window(
    state: WindowState,
) = Window(
    onCloseRequest = state::close,
    icon = rememberVectorPainter(state.icon),
    title = state.title
) {
    if (currentRoute != Route.RequirementsScreen) {
        MenuBar {
            Menu(getStringResource("info.window.menu")) {
                Item(getStringResource("info.window.exit"), onClick = state.exit)
            }
        }
    }
    MaterialTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize()
        ) {
            when (currentRoute) {
                is Route.MainScreen -> MainScreen()
                is Route.RequirementsScreen -> RequirementsScreen()
            }
        }
    }
}