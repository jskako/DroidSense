package ui.application.navigation

sealed class NavRoute {
    data object CheckingRequirementsScreen : NavRoute()
    data object MainScreen : NavRoute()
    data object VariablesScreen : NavRoute()
}