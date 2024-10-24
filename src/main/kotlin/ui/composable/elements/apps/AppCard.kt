package ui.composable.elements.apps

import adb.ApplicationType
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import log.AppData
import log.ApplicationManager
import notifications.InfoManagerData
import ui.application.WindowExtra
import ui.application.WindowStateManager
import ui.application.navigation.WindowData
import ui.composable.elements.BasicTextCaption
import ui.composable.elements.OutlinedButton
import ui.composable.elements.window.TextDialog
import ui.composable.screens.ApplicationDetailsScreen
import utils.Colors.darkRed
import utils.getStringResource

@Composable
fun AppCard(
    applicationManager: ApplicationManager,
    windowStateManager: WindowStateManager,
    deviceModel: String,
    serialNumber: String,
    app: AppData,
    buttonsEnabled: Boolean,
    onMessage: (InfoManagerData) -> Unit,
    onButtonEnabled: (Boolean) -> Unit,
    onAppDeleted: (AppData) -> Unit
) {

    val scope = rememberCoroutineScope()
    var showDialog by remember { mutableStateOf(false) }
    var dialogTitle by remember { mutableStateOf("") }
    var dialogDescription by remember { mutableStateOf("") }
    var onDialogConfirm by remember { mutableStateOf({}) }

    if (showDialog) {
        TextDialog(
            title = dialogTitle,
            description = dialogDescription,
            onConfirmRequest = {
                showDialog = false
                onDialogConfirm()
            },
            onDismissRequest = {
                onButtonEnabled(true)
                showDialog = false
            }
        )
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                BasicTextCaption(
                    text1 = getStringResource("info.app.packageId"),
                    text2 = app.packageId
                )

                Spacer(modifier = Modifier.height(5.dp))

                BasicTextCaption(
                    text1 = getStringResource("info.app.package.path"),
                    text2 = app.appPath ?: ""
                )

                Spacer(modifier = Modifier.height(5.dp))

                BasicTextCaption(
                    text1 = getStringResource("info.app.package.size"),
                    text2 = app.appSize ?: ""
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(
                        space = 10.dp,
                        alignment = Alignment.End
                    )
                ) {
                    OutlinedButton(
                        text = getStringResource("info.app.details"),
                        enabled = buttonsEnabled,
                        onClick = {
                            windowStateManager.windowState?.openNewWindow?.let { newWindow ->
                                newWindow(
                                    WindowData(
                                        title = "$deviceModel ($serialNumber) - ${app.packageId}",
                                        icon = Icons.Default.Info,
                                        windowExtra = WindowExtra(
                                            screen = {
                                                ApplicationDetailsScreen(
                                                    applicationManager = applicationManager,
                                                    packageId = app.packageId
                                                )
                                            },
                                            onClose = {}
                                        )
                                    )
                                )
                            }
                        }
                    )
                    OutlinedButton(
                        text = getStringResource("info.app.clear.data"),
                        enabled = buttonsEnabled,
                        onClick = {
                            onButtonEnabled(false)
                            dialogTitle = "${getStringResource("info.clear.cache.title")}: ${app.packageId}"
                            dialogDescription = getStringResource("info.clear.cache.description")
                            onDialogConfirm = {
                                onMessage(
                                    InfoManagerData(
                                        message = "${getStringResource("info.app.clear.data.started")} ${app.packageId}"
                                    )
                                )
                                scope.launch {
                                    applicationManager.clearAppCache(
                                        packageName = app.packageId
                                    ).fold(
                                        onSuccess = {
                                            onMessage(
                                                InfoManagerData(
                                                    message = "${getStringResource("info.app.clear.data.success")} ${app.packageId}"
                                                )
                                            )
                                        },
                                        onFailure = {
                                            onMessage(
                                                InfoManagerData(
                                                    message = "${getStringResource("info.app.clear.data.failed")} ${app.packageId}",
                                                    color = darkRed
                                                )
                                            )
                                        }
                                    )
                                    onButtonEnabled(true)
                                }
                            }
                            showDialog = true
                        }
                    )

                    OutlinedButton(
                        text = getStringResource(
                            when (app.applicationType) {
                                ApplicationType.SYSTEM -> "info.app.package.force.delete"
                                ApplicationType.USER -> "info.app.package.delete"
                            }
                        ),
                        enabled = buttonsEnabled,
                        contentColor = darkRed,
                        onClick = {
                            onButtonEnabled(false)
                            dialogTitle = "${getStringResource("info.delete.app.title")}: ${app.packageId}"
                            dialogDescription = getStringResource("info.delete.app.description")
                            onDialogConfirm = {
                                onMessage(
                                    InfoManagerData(
                                        message = "${getStringResource("info.app.uninstall.started")} ${app.packageId}"
                                    )
                                )
                                scope.launch {
                                    applicationManager.uninstallApp(
                                        packageName = app.packageId
                                    ).fold(
                                        onSuccess = {
                                            onAppDeleted(app)
                                            onMessage(
                                                InfoManagerData(
                                                    message = "${getStringResource("info.app.uninstall.success")} ${app.packageId}"
                                                )
                                            )
                                        },
                                        onFailure = {
                                            onMessage(
                                                InfoManagerData(
                                                    message = "${getStringResource("info.app.uninstall.failed")} ${app.packageId}",
                                                    color = darkRed
                                                )
                                            )
                                        }
                                    )
                                    onButtonEnabled(true)
                                }
                            }
                            showDialog = true
                        }
                    )
                }
            }
        }
    }
}