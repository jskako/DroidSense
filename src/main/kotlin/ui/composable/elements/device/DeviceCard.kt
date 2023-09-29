package ui.composable.elements.device

import adb.DeviceDetails
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import log.LogManager
import notifications.InfoManager.showTimeLimitedInfoMessage
import ui.application.WindowExtra
import ui.application.WindowStateManager.windowState
import ui.composable.BasicTextCaption
import ui.composable.elements.BasicText
import ui.composable.elements.OutlinedButton
import ui.composable.screens.LogScreen
import utils.DEFAULT_PHONE_IMAGE
import utils.IMAGES_DIRECTORY
import utils.capitalizeFirstChar
import utils.getImageBitmap
import utils.getStringResource
import utils.startScrCpy

@Composable
fun DeviceCard(
    device: DeviceDetails
) {
    val scope = rememberCoroutineScope()
    lateinit var logManager: LogManager
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = 4.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                bitmap = getImageBitmap("$IMAGES_DIRECTORY/$DEFAULT_PHONE_IMAGE"),
                contentDescription = "",
                modifier = Modifier.size(48.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column {

                Row {
                    BasicText(
                        value = "${device.manufacturer?.capitalizeFirstChar()} ${device.model}",
                        fontSize = 20.sp,
                        isBold = true
                    )
                }

                addSpaceHeight(16.dp)

                BasicTextCaption(
                    text1 = getStringResource("info.serial.number"),
                    text2 = device.serialNumber
                )

                addSpaceHeight()

                BasicTextCaption(
                    text1 = getStringResource("info.android.version"),
                    text2 = "${device.androidVersion} (${getStringResource("info.build.sdk")} ${device.buildSDK})"
                )

                addSpaceHeight()

                BasicTextCaption(
                    text1 = getStringResource("info.display.resolution"),
                    text2 = "${
                        (device.displayResolution?.split(": ")?.get(1) ?: "")
                    } (${(device.displayDensity?.split(": ")?.get(1) ?: "")} ppi)"
                )

                addSpaceHeight()

                BasicTextCaption(
                    text1 = getStringResource("info.ip.address"),
                    text2 = device.ipAddress ?: ""
                )

                addSpaceHeight(16.dp)

                Text(
                    text = "${getStringResource("info.device.state")}: ${device.state}",
                    color = Color.Gray
                )
            }

            Column(
                modifier = Modifier.padding(start = 16.dp, end = 16.dp)
            ) {
                OutlinedButton(
                    text = getStringResource("info.share.screen"),
                    onClick = { startScrCpy(device.serialNumber) }
                )

                Spacer(modifier = Modifier.height(16.dp))

                getStringResource("info.log.manager").also { title ->
                    OutlinedButton(
                        text = title,
                        onClick = {
                            windowState?.openNewWindow?.let {
                                it(
                                    title,
                                    Icons.Default.Info,
                                    WindowExtra(
                                        screen = {
                                            logManager = LogScreen(device)
                                        },
                                        onClose = {
                                            scope.launch {
                                                if (logManager.isActive) {
                                                    logManager.stopMonitoringLogs()
                                                    showTimeLimitedInfoMessage(getStringResource("info.log.closing"))
                                                }
                                            }
                                        }
                                    )
                                )
                            }
                        }
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedButton(
                    text = getStringResource("info.log.options"),
                    onClick = { }
                )
            }
        }
    }
}

@Composable
fun addSpaceHeight(height: Dp = 5.dp) {
    Spacer(modifier = Modifier.height(height))
}