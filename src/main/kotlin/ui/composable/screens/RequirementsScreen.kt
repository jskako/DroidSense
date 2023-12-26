package ui.composable.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import requirements.RequirementsManager
import settitngs.GlobalVariables
import ui.composable.sections.RequirementsSection

@Composable
fun RequirementsScreen(
    globalVariables: GlobalVariables,
    navigateToMainScreen: () -> Unit
) {
    val requirementsManager = remember {
        RequirementsManager(
            globalVariables = globalVariables
        )
    }
    Column {
        RequirementsSection(
            requirementsManager = requirementsManager
        )
    }

    rememberCoroutineScope().launch {
        requirementsManager.executeRequirements().fold(
            onSuccess = {
                navigateToMainScreen()
            },
            onFailure = {
                // TODO - add failure logic
            }
        )
    }
}
