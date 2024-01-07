package ui.composable.elements.window

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.window.Window
import java.awt.Dimension
import settitngs.GlobalVariables
import ui.application.WindowState
import ui.application.WindowStateManager
import ui.application.navigation.NavRoute
import ui.application.navigation.NavigationManager
import ui.composable.screens.MainScreen
import ui.composable.screens.RequirementsScreen
import ui.composable.screens.VariablesScreen
import ui.composable.utils.createMenu
import utils.MIN_WINDOW_HEIGHT
import utils.MIN_WINDOW_WIDTH

@Composable
fun NavWindow(
    state: WindowState,
    windowStateManager: WindowStateManager
) = Window(
    onCloseRequest = state::close,
    icon = state.icon?.let { rememberVectorPainter(it) },
    title = state.title
) {
    val globalVariables = remember { GlobalVariables() }
    val navigationManager = remember { NavigationManager() }
    window.minimumSize = Dimension(MIN_WINDOW_WIDTH, MIN_WINDOW_HEIGHT)
    if (navigationManager.navRoute != NavRoute.CheckingRequirementsScreen) {
        createMenu(state)
    }
    MaterialTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize()
        ) {
            state.extra.screen?.invoke() ?: when (navigationManager.navRoute) {
                is NavRoute.MainScreen -> {
                    MainScreen(
                        windowStateManager = windowStateManager,
                        globalVariables = globalVariables
                    )
                }

                is NavRoute.CheckingRequirementsScreen -> {
                    RequirementsScreen(
                        globalVariables = globalVariables,
                        navigateToMainScreen = { navigationManager.navigateTo(NavRoute.MainScreen) },
                        navigateToSetVariablesScreen = { navigationManager.navigateTo(NavRoute.VariablesScreen) }
                    )
                }

                is NavRoute.VariablesScreen -> {
                    VariablesScreen(
                        globalVariables = globalVariables,
                        navigateToMainScreen = { navigationManager.navigateTo(NavRoute.MainScreen) },
                    )
                }
            }
        }
    }
}