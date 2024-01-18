package ui.composable.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import data.repository.settings.SettingsSource
import kotlinx.coroutines.launch
import requirements.RequirementsManager
import ui.composable.sections.RequirementsSection

@Composable
fun RequirementsScreen(
    navigateToMainScreen: () -> Unit,
    navigateToSetVariablesScreen: () -> Unit,
    settingsSource: SettingsSource
) {

    val scope = rememberCoroutineScope()

    val requirementsManager = remember {
        RequirementsManager(
            settingsSource = settingsSource
        )
    }

    Column {
        RequirementsSection(
            requirementsManager = requirementsManager
        )
    }

    LaunchedEffect(Unit) {
        scope.launch {
            requirementsManager.executeRequirements().fold(
                onSuccess = {
                    navigateToMainScreen()
                },
                onFailure = {
                    navigateToSetVariablesScreen()
                }
            )
        }
    }
}
