package ui.composable.elements.window

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.window.Window
import com.jskako.DSDatabase
import data.createDriver
import data.repository.settings.SettingsSource
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
import java.awt.Dimension

@Composable
fun NavWindow(
    state: WindowState,
    windowStateManager: WindowStateManager
) = Window(
    onCloseRequest = state::close,
    icon = state.icon?.let { rememberVectorPainter(it) },
    title = state.title
) {

    val navigationManager = remember { NavigationManager() }

    val dsDatabase by remember { mutableStateOf(DSDatabase(createDriver())) }
    val settingsSource by remember { mutableStateOf(SettingsSource(dsDatabase.settingsQueries)) }

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
                        settingsSource = settingsSource
                    )
                }

                is NavRoute.CheckingRequirementsScreen -> {
                    RequirementsScreen(
                        navigateToMainScreen = { navigationManager.navigateTo(NavRoute.MainScreen) },
                        navigateToSetVariablesScreen = { navigationManager.navigateTo(NavRoute.VariablesScreen) },
                        settingsSource = settingsSource
                    )
                }

                is NavRoute.VariablesScreen -> {
                    VariablesScreen(
                        settingsSource = settingsSource,
                        navigateToMainScreen = { navigationManager.navigateTo(NavRoute.MainScreen) },
                    )
                }
            }
        }
    }
}