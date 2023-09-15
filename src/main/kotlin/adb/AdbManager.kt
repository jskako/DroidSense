package adb

import adb.DeviceManager.addDevice
import adb.DeviceManager.clearDevices
import adb.DeviceManager.devices
import adb.DeviceManager.removeDevice
import adb.DeviceManager.updateDevicesStatus
import di.AppModule.provideCoroutineScope
import di.AppModule.provideLogManager
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import utils.ADB_POLLING_INTERVAL_MS
import utils.getStringResource
import java.io.BufferedReader
import java.io.InputStreamReader

object AdbManager : AdbManagerInterface {

    private var monitorJob: Job? = null
    private val coroutineScope = provideCoroutineScope()
    private val logManager = provideLogManager()

    override fun startListening() {
        monitorJob?.cancel()
        monitorJob = coroutineScope.launch {
            clearDevices()
            monitorAdbDevices()
        }
    }

    override fun stopListening() {
        coroutineScope.launch {
            monitorJob?.cancel()
            updateDevicesStatus()
        }
    }

    private suspend fun monitorAdbDevices() {

        while (true) {
            val currentDevices = mutableSetOf<String>()

            runCatching {
                val process = ProcessBuilder("adb", "devices", "-l").start()
                val reader = BufferedReader(InputStreamReader(process.inputStream))

                reader.useLines { lines ->
                    lines.forEach { line ->
                        if (line.matches(Regex(".*device\\s+.*"))) {
                            val parts = line.split("\\s+".toRegex())
                            val serialNumber = parts[0].trim()
                            currentDevices.add(serialNumber)
                            handleNewDevice(serialNumber, devices)
                        }
                    }
                }

                handleDisconnectedDevices(devices, currentDevices)

            }.getOrElse { exception ->
                logManager.addLog("${getStringResource("error.monitor.general")}: $exception")
            }

            delay(ADB_POLLING_INTERVAL_MS)
        }
    }

    private suspend fun handleNewDevice(serialNumber: String, previousDevices: List<DeviceDetails>) {
        if (serialNumber !in previousDevices.map { it.serialNumber }) {
            addDevice(serialNumber)
        }
    }

    private suspend fun handleDisconnectedDevices(previousDevices: List<DeviceDetails>, currentDevices: Set<String>) {
        val disconnectedSerialNumbers = previousDevices.map { it.serialNumber } - currentDevices
        for (serialNumber in disconnectedSerialNumbers) {
            removeDevice(serialNumber)
        }
    }
}

