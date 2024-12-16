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
import com.jskako.droidsense.generated.resources.info_adb_identifier
import com.jskako.droidsense.generated.resources.info_android_version
import com.jskako.droidsense.generated.resources.info_application_manager
import com.jskako.droidsense.generated.resources.info_build_sdk
import com.jskako.droidsense.generated.resources.info_device_connect_general_error
import com.jskako.droidsense.generated.resources.info_device_connection_switch
import com.jskako.droidsense.generated.resources.info_device_connection_type
import com.jskako.droidsense.generated.resources.info_device_ip_incorrect
import com.jskako.droidsense.generated.resources.info_device_ip_success
import com.jskako.droidsense.generated.resources.info_disconnect
import com.jskako.droidsense.generated.resources.info_disconnecting_device
import com.jskako.droidsense.generated.resources.info_display_resolution
import com.jskako.droidsense.generated.resources.info_ip_address
import com.jskako.droidsense.generated.resources.info_log_closing
import com.jskako.droidsense.generated.resources.info_log_manager
import com.jskako.droidsense.generated.resources.info_private_space_info
import com.jskako.droidsense.generated.resources.info_serial_number
import com.jskako.droidsense.generated.resources.info_share_screen
import com.jskako.droidsense.generated.resources.info_share_screen_fail
import com.jskako.droidsense.generated.resources.info_share_screen_info
import com.jskako.droidsense.generated.resources.info_window_no
import com.jskako.droidsense.generated.resources.string_placeholder
import data.ArgsText
import kotlinx.coroutines.launch
import notifications.InfoManagerData
import org.jetbrains.compose.resources.imageResource
import org.jetbrains.compose.resources.stringResource
import ui.application.WindowExtra
import ui.application.WindowStateManager
import ui.application.navigation.WindowData
import ui.composable.elements.BasicText
import ui.composable.elements.BasicTextCaption
import ui.composable.elements.OutlinedButton
import ui.composable.elements.device.Manufacturers.Companion.getManufacturer
import ui.composable.elements.iconButtons.TooltipIconButton
import ui.composable.elements.window.Sources
import ui.composable.screens.ApplicationScreen
import ui.composable.screens.LogScreen
import utils.Colors.darkBlue
import utils.Colors.darkRed
import utils.Colors.lightGray
import utils.EMPTY_STRING
import utils.capitalizeFirstChar
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
                bitmap = imageResource(
                    device.manufacturer?.let { getManufacturer(it).drawable() } ?: Manufacturers.GENERAL.drawable()
                ),
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
                        tooltip = Res.string.info_disconnect,
                        function = {
                            disconnectInProgress = true
                            onMessage(
                                InfoManagerData(
                                    message = ArgsText(
                                        textResId = Res.string.info_disconnecting_device,
                                        formatArgs = listOf(device.toString())
                                    )
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
                    text1 = stringResource(Res.string.info_adb_identifier),
                    text2 = device.deviceIdentifier
                )

                addSpaceHeight()

                BasicTextCaption(
                    text1 = stringResource(Res.string.info_serial_number),
                    text2 = device.serialNumber
                )

                addSpaceHeight()

                BasicTextCaption(
                    text1 = stringResource(Res.string.info_android_version),
                    text2 = "${device.androidVersion} (${stringResource(Res.string.info_build_sdk)} ${device.buildSDK})"
                )

                addSpaceHeight()

                BasicTextCaption(
                    text1 = stringResource(Res.string.info_display_resolution),
                    text2 = "${
                        (device.displayResolution?.split(": ")?.getOrNull(1) ?: EMPTY_STRING)
                    } (${(device.displayDensity?.split(": ")?.getOrNull(1) ?: EMPTY_STRING)} ppi)"
                )

                addSpaceHeight()

                BasicTextCaption(
                    text1 = stringResource(Res.string.info_ip_address),
                    text2 = device.ipAddress ?: EMPTY_STRING
                )

                addSpaceHeight()

                BasicTextCaption(
                    text1 = stringResource(Res.string.info_private_space_info),
                    text2 = device.privateSpaceIdentifier ?: stringResource(Res.string.info_window_no).uppercase()
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
                        text = "${stringResource(Res.string.info_device_connection_type)}: ${connectionType.name}",
                        color = Color.Gray
                    )

                    if (connectionType == ConnectionType.CABLE && !hasMatchingIp) {
                        TooltipIconButton(
                            icon = Icons.Default.SwitchAccessShortcut,
                            tooltip = Res.string.info_device_connection_switch,
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
                                                        message = ArgsText(
                                                            textResId = Res.string.info_device_ip_success
                                                        )
                                                    )
                                                )
                                            },
                                            onFailure = {
                                                onMessage(
                                                    InfoManagerData(
                                                        message = ArgsText(
                                                            textResId = Res.string.info_device_connect_general_error,
                                                            formatArgs = listOf(it.message ?: "")
                                                        )
                                                    )
                                                )
                                            }
                                        )
                                    } ?: onMessage(
                                        InfoManagerData(
                                            message = ArgsText(
                                                textResId = Res.string.info_device_ip_incorrect
                                            )
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
                    text = stringResource(Res.string.info_share_screen),
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
                                            message = ArgsText(
                                                textResId = Res.string.info_share_screen_info,
                                                formatArgs = listOf(device.deviceIdentifier)
                                            )
                                        )
                                    )
                                },
                                onFailure = {
                                    onMessage(
                                        InfoManagerData(
                                            message = ArgsText(
                                                textResId = Res.string.info_share_screen_fail,
                                                formatArgs = listOf("${device.deviceIdentifier}. ${it.message}")
                                            ),
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
                    text = stringResource(Res.string.info_log_manager),
                    onClick = {
                        val logManager = LogManager(adbPath = adbPath)
                        windowStateManager.windowState?.openNewWindow?.let { newWindow ->
                            newWindow(
                                WindowData(
                                    title = ArgsText(
                                        textResId = Res.string.string_placeholder,
                                        formatArgs = listOf("${device.model} (${device.serialNumber})")
                                    ),
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
                                                            message = ArgsText(
                                                                textResId = Res.string.info_log_closing,
                                                                formatArgs = listOf(device.toString())
                                                            )
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
                    text = stringResource(Res.string.info_application_manager),
                    onClick = {
                        windowStateManager.windowState?.openNewWindow?.let { newWindow ->
                            newWindow(
                                WindowData(
                                    title = ArgsText(
                                        textResId = Res.string.string_placeholder,
                                        formatArgs = listOf("${device.model} (${device.serialNumber})")
                                    ),
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