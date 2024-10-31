package ui.composable.screens

import adb.application.AppData
import adb.application.ApplicationManager
import adb.application.ApplicationType
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ui.application.WindowStateManager
import ui.composable.elements.CircularProgressBar
import ui.composable.elements.LinearProgressBar
import ui.composable.elements.apps.AppsView
import utils.getStringResource

@Composable
fun ApplicationScreen(
    windowStateManager: WindowStateManager,
    serialNumber: String,
    deviceModel: String,
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
    var updateRequired by remember { mutableStateOf(ApplicationUpdateStatus.REQUIRED) }

    LaunchedEffect(identifier, updateRequired) {
        if (updateRequired != ApplicationUpdateStatus.NOT_REQUIRED) {
            updateRequired = ApplicationUpdateStatus.IN_PROGRESS
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
            updateRequired = ApplicationUpdateStatus.NOT_REQUIRED
        }
    }

    CircularProgressBar(
        text = getStringResource("info.getting.application"),
        isVisible = userApps.isEmpty() && systemApps.isEmpty()
    )

    if (userApps.isNotEmpty() || systemApps.isNotEmpty()) {
        Column(
            modifier = Modifier.fillMaxSize(),
        ) {
            LinearProgressBar(
                text = getStringResource("info.updating.application"),
                isVisible = updateRequired == ApplicationUpdateStatus.IN_PROGRESS
            )
            DeviceGroup(
                windowStateManager = windowStateManager,
                applicationManager = applicationManager,
                deviceModel = deviceModel,
                serialNumber = serialNumber,
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
                },
                onUpdateRequired = {
                    updateRequired = it
                }
            )
        }
    }
}

@Composable
fun DeviceGroup(
    windowStateManager: WindowStateManager,
    applicationManager: ApplicationManager,
    serialNumber: String,
    deviceModel: String,
    adbPath: String,
    apps: List<AppData>,
    identifier: String,
    onAppDeleted: (AppData) -> Unit,
    onUpdateRequired: (ApplicationUpdateStatus) -> Unit
) {
    AppsView(
        windowStateManager = windowStateManager,
        applicationManager = applicationManager,
        adbPath = adbPath,
        apps = apps,
        identifier = identifier,
        deviceModel = deviceModel,
        serialNumber = serialNumber,
        onAppDeleted = onAppDeleted,
        onAppInstalled = {
            onUpdateRequired(ApplicationUpdateStatus.REQUIRED)
        }
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

enum class ApplicationUpdateStatus {
    REQUIRED, NOT_REQUIRED, IN_PROGRESS
}