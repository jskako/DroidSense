package ui.application.navigation

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

object NavigationManager {

    private var _currentRoute by mutableStateOf<Route>(Route.RequirementsScreen)
    val currentRoute: Route by derivedStateOf { _currentRoute }


    fun navigateTo(route: Route) {
        _currentRoute = route
    }
}