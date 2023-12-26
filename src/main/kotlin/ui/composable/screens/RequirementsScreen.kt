package ui.composable.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import requirements.RequirementsManager.executeRequirements
import ui.composable.sections.RequirementsSection

@Composable
fun RequirementsScreen(
    navigateToMainScreen: () -> Unit
) {
    Column {
        RequirementsSection()
    }

    rememberCoroutineScope().launch {
        executeRequirements().fold(
            onSuccess = {
                navigateToMainScreen()
            },
            onFailure = {
                // TODO - add failure logic
            }
        )
    }
}
