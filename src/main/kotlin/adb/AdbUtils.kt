package adb

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import utils.EMPTY_STRING
import utils.PRIVATE_SPACE_KEY
import utils.spaceIdRegex

suspend fun connectDeviceWirelessly(
    adbPath: String,
    identifier: String,
    deviceIpAddress: String
): Result<Unit> = withContext(Dispatchers.IO) {
    runCatching {
        val devicesProcess = ProcessBuilder(adbPath, "devices", "-l").start()
        val devicesOutput = devicesProcess.inputStream.bufferedReader().readText()
        devicesProcess.waitFor()

        val tcpDeviceIdentifier = "$deviceIpAddress:5555"
        if (devicesOutput.contains(tcpDeviceIdentifier)) {
        } else {
            val tcpipProcess = ProcessBuilder(adbPath, "-s", identifier, "tcpip", "5555").start()
            val tcpipExitCode = tcpipProcess.waitFor()

            if (tcpipExitCode != 0) {
                return@runCatching Result.failure<Unit>(Exception("Failed to set adb to TCP/IP mode."))
            }

            delay(500)
        }

        val connectProcess = ProcessBuilder(adbPath, "connect", tcpDeviceIdentifier).start()
        val connectOutput = connectProcess.inputStream.bufferedReader().readText()
        val connectExitCode = connectProcess.waitFor()

        if (connectExitCode == 0 && connectOutput.contains("connected", ignoreCase = true)) {
            Result.success(Unit)
        } else {
            Result.failure(Exception("Failed to connect to device at $tcpDeviceIdentifier"))
        }
    }.getOrElse { Result.failure(it) }
}

suspend fun disconnectDevice(
    adbPath: String,
    identifier: String
): Result<Unit> = withContext(Dispatchers.IO) {
    runCatching {
        val disconnectProcess = ProcessBuilder(adbPath, "disconnect", identifier).start()
        val disconnectExitCode = disconnectProcess.waitFor()

        if (disconnectExitCode == 0) {
            Result.success(Unit)
        } else {
            Result.failure(Exception("Failed to disconnect device at $identifier"))
        }
    }.getOrElse { Result.failure(it) }
}

fun getPrivateSpaceId(adbPath: String, identifier: String) = getAvailableSpaces(adbPath, identifier)
    .find {
        it.contains(PRIVATE_SPACE_KEY, ignoreCase = true)
    }
    ?.let { match ->
        spaceIdRegex.find(match)?.groupValues?.get(1)
    }

fun getAvailableSpaces(adbPath: String, identifier: String): List<String> {
    return runCatching {
        ProcessBuilder(adbPath, "-s", identifier, "shell", "pm", "list", "users").start().inputStream
            .bufferedReader()
            .use { reader ->
                reader.readLines()
            }
    }.getOrNull()?.mapNotNull { line ->
        if (line.contains("UserInfo")) {
            line.trim()
        } else {
            null
        }
    } ?: emptyList()
}

fun getDeviceProperty(
    adbPath: String,
    identifier: String,
    property: String
): String {
    return runCatching {
        ProcessBuilder(adbPath, "-s", identifier, "shell", property).start().inputStream
            .bufferedReader()
            .use { reader ->
                reader.readLine()
            }
    }.getOrNull()?.trim() ?: EMPTY_STRING
}

fun getDevicePropertyList(
    adbPath: String,
    identifier: String,
    property: String,
    startingItem: String? = null
): List<String> {
    return runCatching {
        val process = ProcessBuilder(adbPath, "-s", identifier, "shell", property).start()
        val lines = process.inputStream
            .bufferedReader()
            .lineSequence()
            .filter { it.startsWith("package:") }
            .map { it.removePrefix("package:") }
            .toList()
            .sorted()
        process.waitFor()
        return (startingItem?.let {
            mutableListOf(it).apply {
                addAll(lines)
            }
        } ?: lines)
    }.getOrElse {
        it.printStackTrace()
        emptyList()
    }
}