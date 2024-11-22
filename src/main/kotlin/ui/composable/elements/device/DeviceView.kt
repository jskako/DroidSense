package ui.composable.elements.device

import adb.DeviceDetails
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import notifications.InfoManagerData
import ui.application.WindowStateManager
import ui.composable.elements.ListWithScrollbar
import ui.composable.elements.window.Sources

@Composable
fun DeviceView(
    sources: Sources,
    adbPath: String,
    scrCpyPath: String,
    devices: List<DeviceDetails>,
    onMessage: (InfoManagerData) -> Unit,
    windowStateManager: WindowStateManager
) {

    ListWithScrollbar(
        lazyModifier = Modifier.padding(top = 8.dp),
        content = {
            items(devices) { device ->
                DeviceCard(
                    sources = sources,
                    adbPath = adbPath,
                    scrCpyPath = scrCpyPath,
                    device = device,
                    onMessage = onMessage,
                    windowStateManager = windowStateManager,
                    hasMatchingIp = hasMatchingIpForSerialNumber(
                        devices = devices,
                        serialNumber = device.serialNumber
                    )
                )
            }
        }
    )
}

private fun hasMatchingIpForSerialNumber(devices: List<DeviceDetails>, serialNumber: String): Boolean {
    val matchingDevices = devices.filter { it.serialNumber == serialNumber }

    return matchingDevices.any { device ->
        val deviceIdentifierWithoutPort = device.deviceIdentifier.substringBefore(":")
        deviceIdentifierWithoutPort == device.ipAddress
    }
}