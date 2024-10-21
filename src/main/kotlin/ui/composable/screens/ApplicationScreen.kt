package ui.composable.screens

import adb.ApplicationType
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import log.AppData
import log.ApplicationManager
import ui.composable.elements.CircularProgressBar
import ui.composable.elements.apps.AppsView
import utils.getStringResource

@Composable
fun ApplicationScreen(
    adbPath: String,
    identifier: String
) {

    val applicationManager by remember {
        mutableStateOf(ApplicationManager(adbPath = adbPath))
    }

    val userApps = rememberAppsData(applicationManager, identifier, ApplicationType.USER)
    val systemApps = rememberAppsData(applicationManager, identifier, ApplicationType.SYSTEM)

    CircularProgressBar(
        text = getStringResource("info.getting.application"),
        isVisible = userApps.value.isEmpty() && systemApps.value.isEmpty()
    )

    if (userApps.value.isNotEmpty() || systemApps.value.isNotEmpty()) {
        DeviceGroup(
            adbPath = adbPath,
            apps = userApps.value + systemApps.value
        )
    }
}

@Composable
fun DeviceGroup(
    adbPath: String,
    apps: List<AppData>
) {
    AppsView(
        adbPath = adbPath,
        apps = apps
    )
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