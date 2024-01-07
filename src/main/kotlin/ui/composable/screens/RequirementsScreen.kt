package ui.composable.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import requirements.RequirementsManager
import settitngs.GlobalVariables
import ui.composable.sections.RequirementsSection

@Composable
fun RequirementsScreen(
    globalVariables: GlobalVariables,
    navigateToMainScreen: () -> Unit,
    navigateToSetVariablesScreen: () -> Unit
) {
    val requirementsManager = remember {
        RequirementsManager(
            globalVariables = globalVariables
        )
    }
    val scope = rememberCoroutineScope()

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
