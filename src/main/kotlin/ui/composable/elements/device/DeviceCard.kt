package ui.composable.elements.device

import adb.ConnectionType
import adb.DeviceDetails
import adb.DeviceOptions
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import log.LogManager
import notifications.InfoManagerData
import ui.application.WindowExtra
import ui.application.WindowStateManager
import ui.composable.elements.BasicText
import ui.composable.elements.BasicTextCaption
import ui.composable.elements.ClickableMenu
import ui.composable.elements.OutlinedButton
import ui.composable.screens.LogScreen
import utils.DEFAULT_PHONE_IMAGE
import utils.EMPTY_STRING
import utils.IMAGES_DIRECTORY
import utils.capitalizeFirstChar
import utils.getImageBitmap
import utils.getStringResource
import utils.installApplication
import utils.isValidIpAddressWithPort
import utils.startScrCpy

@Composable
fun DeviceCard(
    adbPath: String,
    scrCpyPath: String,
    device: DeviceDetails,
    onMessage: (InfoManagerData) -> Unit,
    windowStateManager: WindowStateManager
) {
    val scope = rememberCoroutineScope()

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
            Image(
                bitmap = getImageBitmap("$IMAGES_DIRECTORY/$DEFAULT_PHONE_IMAGE"),
                contentDescription = EMPTY_STRING,
                modifier = Modifier.size(48.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column {

                Row {
                    BasicText(
                        value = "${device.manufacturer?.capitalizeFirstChar()} ${device.model}",
                        fontSize = 20.sp,
                        isBold = true,
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
                        (device.displayResolution?.split(": ")?.get(1) ?: EMPTY_STRING)
                    } (${(device.displayDensity?.split(": ")?.get(1) ?: EMPTY_STRING)} ppi)"
                )

                addSpaceHeight()

                BasicTextCaption(
                    text1 = getStringResource("info.ip.address"),
                    text2 = device.ipAddress ?: EMPTY_STRING
                )

                addSpaceHeight()

                BasicTextCaption(
                    text1 = getStringResource("info.private.space.info"),
                    text2 = device.privateSpaceIdentifier ?: getStringResource("info.window.no").uppercase()
                )

                addSpaceHeight(16.dp)

                val connectionType by remember {
                    mutableStateOf(
                        isValidIpAddressWithPort(device.deviceIdentifier).let {
                            if (it) ConnectionType.WIRELESS else ConnectionType.CABLE
                        }.name
                    )
                }

                Text(
                    text = "${getStringResource("info.device.connection.type")}: $connectionType",
                    color = Color.Gray
                )
            }

            Column(
                modifier = Modifier.padding(start = 16.dp, end = 16.dp)
            ) {
                OutlinedButton(
                    text = getStringResource("info.share.screen"),
                    onClick = {
                        startScrCpy(
                            scrCpyPath = scrCpyPath,
                            identifier = device.deviceIdentifier
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    text = getStringResource("info.log.manager"),
                    onClick = {
                        val logManager = LogManager(adbPath = adbPath)
                        windowStateManager.windowState?.openNewWindow?.let {
                            it(
                                "${device.model} (${device.serialNumber})",
                                Icons.Default.Info,
                                WindowExtra(
                                    screen = {
                                        LogScreen(
                                            adbPath = adbPath,
                                            device = device,
                                            logManager = logManager,
                                            onMessage = onMessage
                                        )
                                    },
                                    onClose = {
                                        scope.launch {
                                            if (logManager.isActive) {
                                                logManager.stopMonitoringLogs()
                                                onMessage(
                                                    InfoManagerData(
                                                        message = "${getStringResource("info.log.closing")} $device"
                                                    )
                                                )
                                            }
                                        }
                                    }
                                )
                            )
                        }
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                ClickableMenu(
                    text = getStringResource("info.log.options"),
                    functions = listOf(
                        DeviceOptions(
                            text = getStringResource("info.install.apk"),
                            function = {
                                scope.launch {
                                    installApplication(
                                        adbPath = adbPath,
                                        device.deviceIdentifier,
                                        onMessage = onMessage
                                    )
                                }
                            }
                        ),
                        DeviceOptions(
                            text = getStringResource("info.install.private.apk"),
                            function = {
                                scope.launch {
                                    installApplication(
                                        adbPath = adbPath,
                                        device.deviceIdentifier,
                                        onMessage = onMessage,
                                        isPrivateSpace = true
                                    )
                                }
                            }
                        ),
                    )
                )
            }
        }
    }
}

@Composable
fun addSpaceHeight(height: Dp = 5.dp) {
    Spacer(modifier = Modifier.height(height))
}