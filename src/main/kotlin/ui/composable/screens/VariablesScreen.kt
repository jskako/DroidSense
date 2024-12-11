package ui.composable.screens

import androidx.compose.runtime.Composable
import com.jskako.droidsense.generated.resources.Res
import com.jskako.droidsense.generated.resources.info_variables_description
import com.jskako.droidsense.generated.resources.info_variables_title
import data.repository.settings.SettingsSource
import org.jetbrains.compose.resources.stringResource
import ui.composable.sections.VariablesSection

@Composable
fun VariablesScreen(
    settingsSource: SettingsSource,
    navigateToMainScreen: () -> Unit,
    cancelButtonEnabled: Boolean = false
) {
    VariablesSection(
        title = stringResource(Res.string.info_variables_title),
        description = stringResource(Res.string.info_variables_description),
        cancelButtonEnabled = cancelButtonEnabled,
        settingsSource = settingsSource,
        navigateToMainScreen = navigateToMainScreen
    )
}