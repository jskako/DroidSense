package ui.composable.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import requirements.RequirementsManager.executeRequirements
import ui.application.navigation.NavRoute
import ui.application.navigation.NavigationManager.navigateTo
import ui.composable.sections.RequirementsSection

@Composable
fun RequirementsScreen() {
    Column {
        RequirementsSection()
    }

    rememberCoroutineScope().launch {
        executeRequirements().fold(
            onSuccess = {
                navigateTo(NavRoute.MainScreen)
            },
            onFailure = {
                // TODO - add failure logic
            }
        )
    }
}
