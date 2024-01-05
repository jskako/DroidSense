package ui.composable.screens

import androidx.compose.runtime.Composable
import settitngs.GlobalVariables
import ui.composable.sections.VariablesSection
import utils.getStringResource

@Composable
fun VariablesScreen(
    globalVariables: GlobalVariables,
    navigateToMainScreen: () -> Unit,
    cancelButtonEnabled: Boolean = false
) {
    VariablesSection(
        title = getStringResource("info.variables.title"),
        description = getStringResource("info.variables.description"),
        cancelButtonEnabled = cancelButtonEnabled,
        globalVariables = globalVariables,
        navigateToMainScreen = navigateToMainScreen
    )
}