package ui.composable.elements.device

import adb.ConnectionType
import adb.DeviceDetails
import adb.connectDeviceWirelessly
import adb.disconnectDevice
import adb.log.LogManager
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.Eject
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.SwitchAccessShortcut
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
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
import androidx.compose.ui.unit.sp
import com.jskako.droidsense.generated.resources.Res
import com.jskako.droidsense.generated.resources.phone
import kotlinx.coroutines.launch
import notifications.InfoManagerData
import org.jetbrains.compose.resources.imageResource
import ui.application.WindowExtra
import ui.application.WindowStateManager
import ui.application.navigation.WindowData
import ui.composable.elements.BasicText
import ui.composable.elements.BasicTextCaption
import ui.composable.elements.OutlinedButton
import ui.composable.elements.iconButtons.TooltipIconButton
import ui.composable.elements.window.Sources
import ui.composable.screens.ApplicationScreen
import ui.composable.screens.LogScreen
import utils.Colors.darkBlue
import utils.Colors.darkRed
import utils.Colors.lightGray
import utils.EMPTY_STRING
import utils.capitalizeFirstChar
import utils.getStringResource
import utils.isValidIpAddressWithPort
import utils.shareScreen

@Composable
fun DeviceCard(
    sources: Sources,
    adbPath: String,
    scrCpyPath: String,
    device: DeviceDetails,
    onMessage: (InfoManagerData) -> Unit,
    windowStateManager: WindowStateManager,
    hasMatchingIp: Boolean
) {
    val scope = rememberCoroutineScope()
    var disconnectInProgress by remember(device) { mutableStateOf(false) }

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
                bitmap = imageResource(Res.drawable.phone),
                contentDescription = EMPTY_STRING,
                modifier = Modifier.size(48.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TooltipIconButton(
                        isEnabled = !disconnectInProgress,
                        tint = if (disconnectInProgress) lightGray else darkBlue,
                        icon = Icons.Default.Eject,
                        tooltip = getStringResource("info.disconnect"),
                        function = {
                            disconnectInProgress = true
                            onMessage(
                                InfoManagerData(
                                    message = "${getStringResource("info.disconnecting.device")}: $device"
                                )
                            )
                            scope.launch {
                                disconnectDevice(
                                    adbPath = adbPath,
                                    identifier = device.deviceIdentifier
                                )
                            }
                        }
                    )

                    BasicText(
                        value = "${device.manufacturer?.capitalizeFirstChar()} ${device.model}",
                        fontSize = 20.sp,
                        isBold = true,
                    )
                }

                addSpaceHeight(16.dp)

                BasicTextCaption(
                    text1 = getStringResource("info.adb.identifier"),
                    text2 = device.deviceIdentifier
                )

                addSpaceHeight()

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
                        (device.displayResolution?.split(": ")?.getOrNull(1) ?: EMPTY_STRING)
                    } (${(device.displayDensity?.split(": ")?.getOrNull(1) ?: EMPTY_STRING)} ppi)"
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

                val connectionType = when {
                    isValidIpAddressWithPort(device.deviceIdentifier) -> ConnectionType.WIRELESS
                    else -> ConnectionType.CABLE
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "${getStringResource("info.device.connection.type")}: ${connectionType.name}",
                        color = Color.Gray
                    )

                    if (connectionType == ConnectionType.CABLE && !hasMatchingIp) {
                        TooltipIconButton(
                            icon = Icons.Default.SwitchAccessShortcut,
                            tooltip = getStringResource("info.device.connection.switch"),
                            function = {
                                scope.launch {
                                    device.ipAddress?.let {
                                        connectDeviceWirelessly(
                                            adbPath = adbPath,
                                            deviceIpAddress = device.ipAddress,
                                            identifier = device.deviceIdentifier
                                        ).fold(
                                            onSuccess = {
                                                onMessage(
                                                    InfoManagerData(
                                                        message = getStringResource("info.device.ip.success")
                                                    )
                                                )
                                            },
                                            onFailure = {
                                                onMessage(
                                                    InfoManagerData(
                                                        message = "${getStringResource("info.device.connect.general.error")} $it"
                                                    )
                                                )
                                            }
                                        )
                                    } ?: onMessage(
                                        InfoManagerData(
                                            message = getStringResource("info.device.ip.incorrect")
                                        )
                                    )
                                }
                            }
                        )
                    }
                }
            }

            Column(
                modifier = Modifier.padding(start = 16.dp, end = 16.dp)
            ) {
                OutlinedButton(
                    text = getStringResource("info.share.screen"),
                    onClick = {
                        scope.launch {
                            shareScreen(
                                scrCpyPath = scrCpyPath,
                                identifier = device.deviceIdentifier,
                                adbPath = adbPath
                            ).fold(
                                onSuccess = {
                                    onMessage(
                                        InfoManagerData(
                                            message = "${getStringResource("info.share.screen.info")} ${device.deviceIdentifier}"
                                        )
                                    )
                                },
                                onFailure = {
                                    onMessage(
                                        InfoManagerData(
                                            message = "${getStringResource("info.share.screen.fail")} ${device.deviceIdentifier}. ${it.message}",
                                            color = darkRed
                                        )
                                    )
                                }
                            )
                        }
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
                        windowStateManager.windowState?.openNewWindow?.let { newWindow ->
                            newWindow(
                                WindowData(
                                    title = "${device.model} (${device.serialNumber})",
                                    icon = Icons.Default.Info,
                                    windowExtra = WindowExtra(
                                        screen = {
                                            LogScreen(
                                                sources = sources,
                                                adbPath = adbPath,
                                                device = device,
                                                logManager = logManager
                                            )
                                        },
                                        onClose = {
                                            scope.launch {
                                                if (logManager.isActive) {
                                                    logManager.stopMonitoring()
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
                            )
                        }
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    text = getStringResource("info.application.manager"),
                    onClick = {
                        windowStateManager.windowState?.openNewWindow?.let { newWindow ->
                            newWindow(
                                WindowData(
                                    title = "${device.model} (${device.serialNumber})",
                                    icon = Icons.Default.Apps,
                                    windowExtra = WindowExtra(
                                        screen = {
                                            ApplicationScreen(
                                                deviceModel = device.model ?: "",
                                                windowStateManager = windowStateManager,
                                                serialNumber = device.serialNumber,
                                                identifier = device.deviceIdentifier,
                                                adbPath = adbPath
                                            )
                                        },
                                        onClose = {
                                        }
                                    )
                                )
                            )
                        }
                    }
                )
            }
        }
    }
}

@Composable
private fun addSpaceHeight(height: Dp = 5.dp) {
    Spacer(modifier = Modifier.height(height))
}