package ui.application.navigation

sealed class Route {
    data object RequirementsScreen : Route()
    data object MainScreen : Route()
}