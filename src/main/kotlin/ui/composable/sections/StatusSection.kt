package ui.composable.sections

import adb.DeviceManager
import adb.DeviceOptions
import adb.MonitorStatus
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.RunCircle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import notifications.InfoManagerData
import ui.application.WindowExtra
import ui.application.WindowStateManager
import ui.composable.elements.ClickableIconMenu
import ui.composable.elements.iconButtons.IconClickableText
import utils.Colors.darkGreen
import utils.Colors.darkRed
import utils.getStringResource
import utils.startScrCpy

@Composable
fun StatusSection(
    scrCpyPath: String,
    deviceManager: DeviceManager,
    onMessage: (InfoManagerData) -> Unit,
    windowStateManager: WindowStateManager
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

                if (deviceManager.devices.isNotEmpty()) {
                    Spacer(modifier = Modifier.width(16.dp))

                    Text(
                        text = "${getStringResource("info.device.number")}: ${deviceManager.devices.size}",
                        textAlign = TextAlign.Center
                    )
                }
            }

            ClickableIconMenu(
                icon = Icons.Default.Menu,
                functions = listOf(
                    DeviceOptions(
                        text = getStringResource("info.log.history"),
                        function = {
                            windowStateManager.windowState?.openNewWindow?.let {
                                it(
                                    getStringResource("info.log.history"),
                                    Icons.Default.History,
                                    WindowExtra(
                                        screen = {

                                        }
                                    )
                                )
                            }
                        }
                    ),
                    DeviceOptions(
                        text = getStringResource("info.share.all.screens"),
                        function = {
                            deviceManager.devices.forEach {
                                startScrCpy(
                                    scrCpyPath = scrCpyPath,
                                    serialNumber = it.serialNumber
                                )
                            }
                        },
                        enabled = deviceManager.devices.isNotEmpty()
                    )
                )
            )
        }
    }
}