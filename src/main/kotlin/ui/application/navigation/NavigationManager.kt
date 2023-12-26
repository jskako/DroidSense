package ui.application.navigation

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class NavigationManager : NavigationInterface {

    private var _navRoute by mutableStateOf<NavRoute>(NavRoute.RequirementsScreen)
    val navRoute: NavRoute by derivedStateOf { _navRoute }

    override fun navigateTo(navRoute: NavRoute) {
        _navRoute = navRoute
    }
}