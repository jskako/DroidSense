package ui.composable.elements.apps

import adb.application.AppData
import adb.application.ApplicationManager
import adb.application.ApplicationType
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
import com.jskako.droidsense.generated.resources.Res
import com.jskako.droidsense.generated.resources.info_app_clear_data
import com.jskako.droidsense.generated.resources.info_app_clear_data_failed
import com.jskako.droidsense.generated.resources.info_app_clear_data_started
import com.jskako.droidsense.generated.resources.info_app_clear_data_success
import com.jskako.droidsense.generated.resources.info_app_details
import com.jskako.droidsense.generated.resources.info_app_packageId
import com.jskako.droidsense.generated.resources.info_app_package_delete
import com.jskako.droidsense.generated.resources.info_app_package_force_delete
import com.jskako.droidsense.generated.resources.info_app_package_path
import com.jskako.droidsense.generated.resources.info_app_package_size
import com.jskako.droidsense.generated.resources.info_app_uninstall_failed
import com.jskako.droidsense.generated.resources.info_app_uninstall_started
import com.jskako.droidsense.generated.resources.info_app_uninstall_success
import com.jskako.droidsense.generated.resources.info_clear_cache_description
import com.jskako.droidsense.generated.resources.info_clear_cache_title
import com.jskako.droidsense.generated.resources.info_delete_app_description
import com.jskako.droidsense.generated.resources.info_delete_app_title
import com.jskako.droidsense.generated.resources.info_force_delete_app_description
import com.jskako.droidsense.generated.resources.string_placeholder
import data.ArgsText
import kotlinx.coroutines.launch
import notifications.InfoManagerData
import org.jetbrains.compose.resources.stringResource
import ui.application.WindowExtra
import ui.application.WindowStateManager
import ui.application.navigation.WindowData
import ui.composable.elements.BasicTextCaption
import ui.composable.elements.OutlinedButton
import ui.composable.elements.window.TextDialog
import ui.composable.screens.ApplicationDetailsScreen
import utils.Colors.darkRed

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
    var dialogTitle by remember { mutableStateOf(ArgsText()) }
    var dialogDescription by remember { mutableStateOf(ArgsText()) }
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
                    text1 = stringResource(Res.string.info_app_packageId),
                    text2 = app.packageId
                )

                Spacer(modifier = Modifier.height(5.dp))

                BasicTextCaption(
                    text1 = stringResource(Res.string.info_app_package_path),
                    text2 = app.appPath ?: ""
                )

                Spacer(modifier = Modifier.height(5.dp))

                BasicTextCaption(
                    text1 = stringResource(Res.string.info_app_package_size),
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
                        text = stringResource(Res.string.info_app_details),
                        enabled = buttonsEnabled,
                        onClick = {
                            windowStateManager.windowState?.openNewWindow?.let { newWindow ->
                                newWindow(
                                    WindowData(
                                        title = ArgsText(
                                            textResId = Res.string.string_placeholder,
                                            formatArgs = listOf("$deviceModel ($serialNumber) - ${app.packageId}")
                                        ),
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
                        text = stringResource(Res.string.info_app_clear_data),
                        enabled = buttonsEnabled,
                        onClick = {
                            onButtonEnabled(false)
                            dialogTitle = ArgsText(
                                textResId = Res.string.info_clear_cache_title,
                                formatArgs = listOf(app.packageId)
                            )
                            dialogDescription = ArgsText(
                                textResId = Res.string.info_clear_cache_description
                            )
                            onDialogConfirm = {
                                onMessage(
                                    InfoManagerData(
                                        message = ArgsText(
                                            textResId = Res.string.info_app_clear_data_started,
                                            formatArgs = listOf(app.packageId)
                                        )
                                    )
                                )
                                scope.launch {
                                    applicationManager.clearAppCache(
                                        packageName = app.packageId
                                    ).fold(
                                        onSuccess = {
                                            onMessage(
                                                InfoManagerData(
                                                    message = ArgsText(
                                                        textResId = Res.string.info_app_clear_data_success,
                                                        formatArgs = listOf(app.packageId)
                                                    )
                                                )
                                            )
                                        },
                                        onFailure = {
                                            onMessage(
                                                InfoManagerData(
                                                    message = ArgsText(
                                                        textResId = Res.string.info_app_clear_data_failed,
                                                        formatArgs = listOf(app.packageId)
                                                    ),
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
                        text = stringResource(
                            when (app.applicationType) {
                                ApplicationType.SYSTEM -> Res.string.info_app_package_force_delete
                                ApplicationType.USER -> Res.string.info_app_package_delete
                            }
                        ),
                        enabled = buttonsEnabled,
                        contentColor = darkRed,
                        onClick = {
                            onButtonEnabled(false)
                            dialogTitle = ArgsText(
                                textResId = Res.string.info_delete_app_title,
                                formatArgs = listOf(app.packageId)
                            )
                            dialogDescription = when (app.applicationType) {
                                ApplicationType.SYSTEM -> ArgsText(Res.string.info_force_delete_app_description)
                                ApplicationType.USER -> ArgsText(Res.string.info_delete_app_description)
                            }

                            onDialogConfirm = {
                                onMessage(
                                    InfoManagerData(
                                        message = ArgsText(
                                            textResId = Res.string.info_app_uninstall_started,
                                            formatArgs = listOf(app.packageId)
                                        )
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
                                                    message = ArgsText(
                                                        textResId = Res.string.info_app_uninstall_success,
                                                        formatArgs = listOf(app.packageId)
                                                    )
                                                )
                                            )
                                        },
                                        onFailure = {
                                            onMessage(
                                                InfoManagerData(
                                                    message = ArgsText(
                                                        textResId = Res.string.info_app_uninstall_failed,
                                                        formatArgs = listOf(app.packageId)
                                                    ),
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