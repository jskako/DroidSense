package utils

import di.AppModule.provideResourceBundle
import java.awt.Desktop
import java.awt.FileDialog
import java.awt.Frame
import java.io.File
import java.net.URL
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Locale
import java.util.regex.Pattern
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.withContext
import notifications.InfoManager.showTimeLimitedInfoMessage
import notifications.LogManager.addLog

fun getStringResource(resourceName: String) =
    provideResourceBundle(STRING_RESOURCES).getString(resourceName) ?: EMPTY_STRING

fun getRegexPattern(regex: String): Pattern = Pattern.compile(regex)
fun isValidIPAddress(ip: String) = getRegexPattern(ipRegex).matcher(ip).matches()
fun isValidNumber(port: String) = getRegexPattern(NUMBER_REGEX).matcher(port).matches()

fun getTimeStamp(format: String = DEFAULT_TIMESTAMP): String = DateTimeFormatter
    .ofPattern(format)
    .withZone(ZoneOffset.UTC)
    .format(LocalDateTime.now())

fun openFile(path: String) {
    val result = runCatching {
        val file = File(path)
        check(Desktop.isDesktopSupported()) { getStringResource("error.openfile.desktop.unsupported") }
        check(file.exists()) { getStringResource("error.openfile.no.file") }
        Desktop.getDesktop().open(file)
    }

    result.getOrElse { e ->
        addLog("${getStringResource("error.openfile.cannot.open")} $path\n${e.message}")
    }.let {
        showTimeLimitedInfoMessage("${getStringResource("success.openfile.general")}: $path")
    }
}

suspend fun isInternetAvailable(): Result<String> = withContext(Default) {
    runCatching {
        URL(DEFAULT_WEB).openConnection().connect()
        Result.success(getStringResource("success.network.general"))
    }.getOrElse {
        Result.failure(
            IllegalArgumentException(
                "${getStringResource("error.network.not.unavailable")}\n" +
                        "${it.message}"
            )
        )
    }
}

fun emptyLine(numberOfEmptyLines: Int = 1) =
    buildString {
        repeat(numberOfEmptyLines) {
            appendLine()
        }
    }

fun pickDirectoryDialog(): String? {
    val fileDialog = FileDialog(Frame(), getStringResource("info.directory.general"), FileDialog.LOAD).apply {
        mode = FileDialog.SAVE
        isMultipleMode = false
        isVisible = true
    }

    return if (!fileDialog.directory.isNullOrEmpty() && !fileDialog.file.isNullOrEmpty()) {
        "${fileDialog.directory}/${fileDialog.file}"
    } else {
        null
    }
}

fun getUserOS(): String {
    val osName = System.getProperty(SYSTEM_OS_PROPERTY).lowercase(Locale.getDefault())
    return OS.entries.firstOrNull { osName.contains(it.osName(), ignoreCase = true) }
        ?.osName() ?: OS.UNSUPPORTED.osName()
}

fun isSoftwareInstalled(software: String) = ProcessBuilder("which", software).start().waitFor() == 0
