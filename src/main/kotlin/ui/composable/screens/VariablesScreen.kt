package ui.composable.screens

import androidx.compose.runtime.Composable
import data.repository.SettingsSource
import ui.composable.sections.VariablesSection
import utils.getStringResource

@Composable
fun VariablesScreen(
    settingsSource: SettingsSource,
    navigateToMainScreen: () -> Unit,
    cancelButtonEnabled: Boolean = false
) {
    VariablesSection(
        title = getStringResource("info.variables.title"),
        description = getStringResource("info.variables.description"),
        cancelButtonEnabled = cancelButtonEnabled,
        settingsSource = settingsSource,
        navigateToMainScreen = navigateToMainScreen
    )
}