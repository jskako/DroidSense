package ui.composable.screens

import adb.ApplicationType
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import log.AppData
import log.ApplicationManager
import notifications.InfoManager
import notifications.InfoManagerData

@Composable
fun ApplicationScreen(
    adbPath: String,
    identifier: String
) {

    val scope = rememberCoroutineScope()
    val infoManager = remember { InfoManager() }
    val applicationManager by remember {
        mutableStateOf(ApplicationManager(adbPath = adbPath))
    }

    val userApps = rememberAppsData(applicationManager, identifier, ApplicationType.USER)
    val systemApps = rememberAppsData(applicationManager, identifier, ApplicationType.SYSTEM)

    fun showMessage(message: String) {
        infoManager.showMessage(
            infoManagerData = InfoManagerData(
                message = message
            ),
            scope = scope
        )
    }

    fun showMessage(infoManagerData: InfoManagerData) {
        infoManager.showMessage(
            infoManagerData = infoManagerData,
            scope = scope
        )
    }

    Column {

    }
}

@Composable
private fun rememberAppsData(
    applicationManager: ApplicationManager,
    identifier: String,
    applicationType: ApplicationType
): State<List<AppData>> {
    return produceState(initialValue = emptyList(), applicationType) {
        applicationManager.getAppsData(
            identifier = identifier,
            applicationType = applicationType
        ) { appsList ->
            value = appsList
        }
    }
}