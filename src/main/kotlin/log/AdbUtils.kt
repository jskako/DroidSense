package log

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.withContext
import notifications.InfoManagerData
import utils.APK_EXTENSION
import utils.Colors.darkBlue
import utils.Colors.darkRed
import utils.EMPTY_STRING
import utils.getStringResource
import utils.pickFile

suspend fun uninstallApp(
    adbPath: String,
    packageName: String
): Boolean = withContext(Dispatchers.IO) {
    val process = ProcessBuilder(adbPath, "shell", "pm", "uninstall", packageName).start()

    return@withContext runCatching {
        process.inputStream.bufferedReader().useLines { lines ->
            lines.any { it.contains("Success") }
        }
    }.getOrElse { false }
}

suspend fun clearAppCache(
    adbPath: String,
    packageName: String
): Boolean = withContext(Dispatchers.IO) {
    val process = ProcessBuilder(adbPath, "shell", "pm", "clear", packageName).start()

    return@withContext runCatching {
        process.inputStream.bufferedReader().useLines { lines ->
            lines.any { it.contains("Success") }
        }
    }.getOrElse { false }
}

suspend fun installApplication(
    adbPath: String,
    identifier: String,
    onMessage: (InfoManagerData) -> Unit,
    isPrivateSpace: Boolean = false
) = withContext(Default) {
    runCatching {
        pickFile(allowedExtension = APK_EXTENSION)?.let { file ->
            onMessage(
                InfoManagerData(
                    message = "${getStringResource("info.file.install")}: ${file.name}",
                    duration = null
                )
            )

            val command = buildAdbInstallCommand(adbPath, identifier, file.absolutePath, isPrivateSpace)

            val process = ProcessBuilder(command).apply { redirectErrorStream(true) }.start()

            val exitCode = process.waitFor()

            val message = if (exitCode == 0) {
                getStringResource("success.file.install")
            } else {
                getStringResource("error.file.install")
            }

            onMessage(
                InfoManagerData(
                    message = "$message: ${file.canonicalPath}",
                    color = if (exitCode == 0) darkBlue else darkRed
                )
            )
        }
    }.getOrElse {
        onMessage(
            InfoManagerData(
                message = getStringResource("error.file.install"),
                color = darkRed
            )
        )
    }
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