package ui.application.navigation

sealed class NavRoute {
    data object RequirementsScreen : NavRoute()
    data object MainScreen : NavRoute()
}