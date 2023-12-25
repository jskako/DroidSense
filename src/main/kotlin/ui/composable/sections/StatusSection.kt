package ui.composable.sections

import adb.AdbDeviceManager.listeningStatus
import adb.AdbDeviceManager.manageListeningStatus
import adb.DeviceManager
import adb.DeviceOptions
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ui.application.WindowExtra
import ui.application.WindowStateManager.windowState
import ui.composable.elements.ClickableIconMenu
import utils.getStringResource
import utils.startScrCpy

@Composable
fun StatusSection(deviceManager: DeviceManager) {
    val scope = rememberCoroutineScope()
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Row {
            Row(
                modifier = Modifier
                    .weight(1f)
                    .wrapContentSize()
                    .clickable {
                        manageListeningStatus(
                            deviceManager = deviceManager,
                            coroutineScope = scope
                        )
                    }
            ) {
                Text(
                    text = "${getStringResource("info.status.general")}: ${listeningStatus.value.status()}",
                    textAlign = TextAlign.Center,
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
                            windowState?.openNewWindow?.let {
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
                                startScrCpy(it.serialNumber)
                            }
                        }
                    )
                )
            )
        }
    }
}