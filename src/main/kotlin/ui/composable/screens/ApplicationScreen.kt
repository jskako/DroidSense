package ui.composable.screens

import adb.ApplicationType
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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

    val scope = rememberCoroutineScope()
    val applicationManager by remember {
        mutableStateOf(
            ApplicationManager(
                adbPath = adbPath,
                identifier = identifier
            )
        )
    }

    var userApps by remember { mutableStateOf<List<AppData>>(emptyList()) }
    var systemApps by remember { mutableStateOf<List<AppData>>(emptyList()) }

    LaunchedEffect(identifier) {
        val userAppsData = async {
            applicationManager.getAppsData(
                applicationType = ApplicationType.USER
            )
        }

        val systemAppsData = async {
            applicationManager.getAppsData(
                applicationType = ApplicationType.SYSTEM
            )
        }

        userApps = userAppsData.await()
        systemApps = systemAppsData.await()
    }

    CircularProgressBar(
        text = getStringResource("info.getting.application"),
        isVisible = userApps.isEmpty() && systemApps.isEmpty()
    )

    if (userApps.isNotEmpty() || systemApps.isNotEmpty()) {
        DeviceGroup(
            applicationManager = applicationManager,
            adbPath = adbPath,
            apps = userApps + systemApps,
            identifier = identifier,
            onAppDeleted = { appData ->
                scope.launch {
                    deleteApplication(
                        appData = appData,
                        list = if (appData.applicationType == ApplicationType.USER) userApps else systemApps,
                        onDone = { updatedList ->
                            if (appData.applicationType == ApplicationType.SYSTEM) {
                                systemApps = updatedList
                            } else {
                                userApps = updatedList
                            }
                        }
                    )
                }
            }
        )
    }
}

@Composable
fun DeviceGroup(
    applicationManager: ApplicationManager,
    adbPath: String,
    apps: List<AppData>,
    identifier: String,
    onAppDeleted: (AppData) -> Unit
) {
    AppsView(
        applicationManager = applicationManager,
        adbPath = adbPath,
        apps = apps,
        identifier = identifier,
        onAppDeleted = onAppDeleted
    )
}

private suspend fun deleteApplication(
    appData: AppData,
    list: List<AppData>,
    onDone: (List<AppData>) -> Unit
) {
    withContext(Dispatchers.Default) {
        val updatedAppData = list.filterNot { app ->
            app.packageId == appData.packageId
        }

        onDone(updatedAppData)
    }
}