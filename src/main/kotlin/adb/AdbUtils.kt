package adb

import utils.EMPTY_STRING
import utils.PRIVATE_SPACE_KEY
import utils.spaceIdRegex

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