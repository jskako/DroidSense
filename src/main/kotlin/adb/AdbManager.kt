package adb

import adb.DeviceManager.addDevice
import adb.DeviceManager.clearDevices
import adb.DeviceManager.devices
import adb.DeviceManager.removeDevice
import adb.DeviceManager.updateDevicesStatus
import java.io.BufferedReader
import java.io.InputStreamReader
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import notifications.LogManager.addLog
import settitngs.GlobalSettings.adbPath
import utils.ADB_POLLING_INTERVAL_MS
import utils.getStringResource

object AdbManager : AdbManagerInterface {

    private var monitorJob: Job? = null

    override fun startListening(coroutineScope: CoroutineScope) {
        monitorJob?.cancel()
        monitorJob = coroutineScope.launch {
            clearDevices()
            monitorAdbDevices()
        }
    }

    override fun stopListening(coroutineScope: CoroutineScope) {
        coroutineScope.launch {
            monitorJob?.cancel()
            updateDevicesStatus()
        }
    }

    private suspend fun monitorAdbDevices() {

        while (true) {
            val currentDevices = mutableSetOf<String>()

            runCatching {
                val process = ProcessBuilder(adbPath.value, "devices", "-l").start()
                val reader = BufferedReader(InputStreamReader(process.inputStream))

                reader.useLines { lines ->
                    lines.forEach { line ->
                        if (line.matches(Regex(".*device\\s+.*"))) {
                            val parts = line.split("\\s+".toRegex())
                            val serialNumber = parts[0].trim()
                            currentDevices.add(serialNumber)
                            handleNewDevice(serialNumber)
                        }
                    }
                }

                handleDisconnectedDevices(currentDevices)

            }.getOrElse { exception ->
                addLog("${getStringResource("error.monitor.general")}: $exception")
            }

            delay(ADB_POLLING_INTERVAL_MS)
        }
    }

    private suspend fun handleNewDevice(serialNumber: String) {
        if (serialNumber !in devices.map { it.serialNumber }) {
            addDevice(serialNumber)
        }
    }

    private suspend fun handleDisconnectedDevices(currentDevices: Set<String>) {
        val disconnectedSerialNumbers = devices.map { it.serialNumber } - currentDevices
        for (serialNumber in disconnectedSerialNumbers) {
            removeDevice(serialNumber)
        }
    }
}

