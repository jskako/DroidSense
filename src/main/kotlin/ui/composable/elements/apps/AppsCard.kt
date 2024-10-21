package ui.composable.elements.apps

import adb.ApplicationType
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import log.AppData
import log.uninstallApp
import notifications.InfoManagerData
import ui.composable.elements.BasicTextCaption
import ui.composable.elements.OutlinedButton
import ui.composable.elements.window.TextDialog
import utils.Colors.darkRed
import utils.getStringResource

@Composable
fun AppsCard(
    adbPath: String,
    app: AppData,
    onMessage: (InfoManagerData) -> Unit
) {

    val scope = rememberCoroutineScope()
    var showDialog by remember { mutableStateOf(false) }

    if (showDialog) {
        TextDialog(
            title = "What do you want from me?",
            description = "Hey, this is just an test",
            onConfirmRequest = { showDialog = true },
            onDismissRequest = {
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

                addSpaceHeight()

                BasicTextCaption(
                    text1 = getStringResource("info.app.package.path"),
                    text2 = app.appPath ?: ""
                )

                addSpaceHeight()

                BasicTextCaption(
                    text1 = getStringResource("info.app.package.size"),
                    text2 = app.appSize ?: ""
                )

                addSpaceHeight(height = 10.dp)

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(
                        space = 10.dp,
                        alignment = Alignment.End
                    )
                ) {
                    OutlinedButton(
                        text = getStringResource("info.app.clear.data"),
                        onClick = {
                            showDialog = true
                            /*scope.launch {
                                clearAppCache(
                                    adbPath = adbPath,
                                    packageName = app.packageId
                                )
                            }*/
                        }
                    )

                    OutlinedButton(
                        text = getStringResource(
                            when (app.applicationType) {
                                ApplicationType.SYSTEM -> "info.app.package.force.delete"
                                ApplicationType.USER -> "info.app.package.delete"
                            }
                        ),
                        color = darkRed,
                        onClick = {
                            scope.launch {
                                when (app.applicationType) {
                                    ApplicationType.SYSTEM -> Unit
                                    ApplicationType.USER -> {
                                        uninstallApp(
                                            adbPath = adbPath,
                                            packageName = app.packageId
                                        )
                                    }
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun addSpaceHeight(height: Dp = 5.dp) {
    Spacer(modifier = Modifier.height(height))
}