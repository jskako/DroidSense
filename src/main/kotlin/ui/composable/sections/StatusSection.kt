package ui.composable.sections

import adb.DeviceDetails
import adb.DeviceManager
import adb.MonitorStatus
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ScreenShare
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.RunCircle
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import data.repository.settings.SettingsSource
import kotlinx.coroutines.launch
import notifications.InfoManagerData
import ui.application.WindowExtra
import ui.application.WindowStateManager
import ui.application.navigation.WindowData
import ui.composable.elements.TooltipTextButton
import ui.composable.elements.iconButtons.IconClickableText
import ui.composable.elements.iconButtons.TooltipIconButton
import ui.composable.screens.SettingsScreen
import utils.Colors.darkGreen
import utils.Colors.darkRed
import utils.getStringResource
import utils.shareScreen

@Composable
fun StatusSection(
    scrCpyPath: String,
    deviceManager: DeviceManager,
    onMessage: (InfoManagerData) -> Unit,
    windowStateManager: WindowStateManager,
    settingsSource: SettingsSource,
    devices: List<DeviceDetails>
) {
    val scope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Row {
            Row(
                modifier = Modifier
                    .weight(1f),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconClickableText(
                    icon = Icons.Default.RunCircle,
                    iconColor = if (deviceManager.isMonitoring()) darkGreen else darkRed,
                    text = "${getStringResource("info.status.general")}: ${deviceManager.monitoringStatus.value.status()}",
                    function = {
                        deviceManager.manageListeningStatus(
                            monitorStatus = if (deviceManager.isMonitoring()) MonitorStatus.STOP else MonitorStatus.START,
                            scope = scope,
                            onMessage = onMessage
                        )
                    }
                )

                if (devices.isNotEmpty()) {
                    Spacer(modifier = Modifier.width(16.dp))

                    TooltipTextButton(
                        plainTooltipPositionProvider = 60.dp,
                        tooltip = devices.filter { it.toString().trim().isNotEmpty() }
                            .joinToString(separator = "\n"),
                        text = "${getStringResource("info.device.number")}: ${devices.size}",
                        function = {}
                    )
                }
            }

            if (devices.size > 1) {
                TooltipIconButton(
                    icon = Icons.AutoMirrored.Filled.ScreenShare,
                    tooltip = getStringResource("info.share.all.screens"),
                    function = {
                        scope.launch {
                            devices.distinctBy { it.serialNumber }.forEach { device ->
                                shareScreen(
                                    scrCpyPath = scrCpyPath,
                                    identifier = device.deviceIdentifier
                                )
                            }
                        }
                    }
                )
            }

            TooltipIconButton(
                icon = Icons.Default.History,
                tooltip = getStringResource("info.history"),
                function = {

                }
            )

            TooltipIconButton(
                icon = Icons.Default.Settings,
                tooltip = getStringResource("info.settings"),
                function = {
                    windowStateManager.windowState?.openNewWindow?.let { newWindow ->
                        newWindow(
                            WindowData(
                                title = getStringResource("info.settings"),
                                icon = Icons.Default.Info,
                                windowExtra = WindowExtra(
                                    screen = {
                                        SettingsScreen(
                                            settingsSource = settingsSource
                                        )
                                    },
                                    onClose = {}
                                )
                            )
                        )
                    }
                }
            )
        }
    }
}