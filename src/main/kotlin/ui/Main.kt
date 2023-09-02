package ui

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.runBlocking
import java.io.BufferedReader
import java.io.InputStreamReader


@Composable
@Preview
fun App() {
    runBlocking {
        monitorAdbDevices().collect { device ->
            println("Device connected:")
            println("Serial Number: ${device.serialNumber}")
            println("Model: ${device.model}")
            println("State: ${device.state}")
        }
    }
}

fun main() = application {
    Window(onCloseRequest = ::exitApplication) {
        App()
    }
}

data class AdbDevice(val serialNumber: String, val model: String, val state: String)

fun monitorAdbDevices(): Flow<AdbDevice> = flow {
    var previousDevices = setOf<String>()

    while (true) {
        val currentDevices = mutableSetOf<String>()

        try {
            val processBuilder = ProcessBuilder("adb", "devices", "-l")
            val process = processBuilder.start()

            val reader = BufferedReader(InputStreamReader(process.inputStream))
            var line: String?

            while (reader.readLine().also { line = it } != null) {
                if (line!!.matches(Regex(".*device\\s+.*"))) {
                    val parts = line!!.split("\\s+".toRegex())
                    val serialNumber = parts[0].trim()
                    currentDevices.add(serialNumber)
                    if (serialNumber !in previousDevices) {
                        val model = getDeviceProperty(serialNumber, "ro.product.model")
                        emit(AdbDevice(serialNumber, model, "Connected"))
                    }
                }
            }

            // Detect disconnected devices
            for (serialNumber in previousDevices) {
                if (serialNumber !in currentDevices) {
                    emit(AdbDevice(serialNumber, "", "Disconnected"))
                }
            }

            previousDevices = currentDevices
        } catch (e: Exception) {
            // Handle exceptions (e.g., ADB not found)
            emit(AdbDevice("", "", "Error: ${e.message}"))
        }

        delay(1000) // Adjust the delay as needed
    }
}.flowOn(Dispatchers.IO)

private fun getDeviceProperty(serialNumber: String, property: String): String {
    val processBuilder = ProcessBuilder("adb", "-s", serialNumber, "shell", "getprop", property)
    val process = processBuilder.start()

    val reader = BufferedReader(InputStreamReader(process.inputStream))
    var line: String? = reader.readLine()
    process.waitFor()
    return line ?: ""
}