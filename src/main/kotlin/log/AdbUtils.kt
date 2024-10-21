package log

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import utils.EMPTY_STRING

suspend fun uninstallApp(
    adbPath: String,
    packageName: String
): Result<Unit> = withContext(Dispatchers.IO) {
    runCatching {
        val process = ProcessBuilder(adbPath, "shell", "pm", "uninstall", packageName).start()
        val isSuccess = process.inputStream.bufferedReader().useLines { lines ->
            lines.any { it.contains("Success") }
        }

        if (isSuccess) {
            Result.success(Unit)
        } else {
            Result.failure(Exception("Failed to uninstall the app"))
        }
    }.getOrElse { Result.failure(it) }
}

suspend fun clearAppCache(
    adbPath: String,
    packageName: String
): Result<Unit> = withContext(Dispatchers.IO) {
    runCatching {
        val process = ProcessBuilder(adbPath, "shell", "pm", "clear", packageName).start()
        val isSuccess = process.inputStream.bufferedReader().useLines { lines ->
            lines.any { it.contains("Success") }
        }

        if (isSuccess) {
            Result.success(Unit)
        } else {
            Result.failure(Exception("Failed to clear the app cache"))
        }
    }.getOrElse { Result.failure(it) }
}

private fun buildAdbInstallCommand(
    adbPath: String,
    identifier: String,
    filePath: String,
    isPrivateSpace: Boolean
): MutableList<String> {
    val command = mutableListOf(adbPath, "-s", identifier, "install")

    if (isPrivateSpace) {
        getPrivateSpaceId(adbPath)?.let { userId ->
            command.add("--user")
            command.add(userId)
        }
    }

    command.addAll(listOf("-r", filePath))
    return command
}

fun getPrivateSpaceId(adbPath: String): String? {
    return runCatching {
        ProcessBuilder(adbPath, "shell", "dumpsys", "user")
            .redirectErrorStream(true)
            .start()
            .inputStream
            .bufferedReader()
            .use { it.readText() }
            .lineSequence()
            .firstOrNull { it.contains("Private space") }
            ?.substringAfter("UserInfo{")
            ?.substringBefore(":")
    }.getOrNull()
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