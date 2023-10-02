package utils

import androidx.compose.ui.res.loadImageBitmap
import androidx.compose.ui.res.useResource
import di.AppModule.provideResourceBundle
import notifications.InfoManager.showTimeLimitedInfoMessage
import settitngs.GlobalSettings
import java.awt.Desktop
import java.awt.FileDialog
import java.awt.Frame
import java.io.File
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Locale

fun getStringResource(resourceName: String) =
    provideResourceBundle(STRING_RESOURCES).getString(resourceName) ?: EMPTY_STRING

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
        showTimeLimitedInfoMessage("${getStringResource("error.openfile.cannot.open")} $path\n${e.message}")
    }.let {
        showTimeLimitedInfoMessage("${getStringResource("success.openfile.general")}: $path")
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

fun getImageBitmap(path: String) = useResource(path) { loadImageBitmap(it) }

fun startScrCpy(serialNumber: String) = ProcessBuilder(GlobalSettings.scrCpyPath.value, "-s", serialNumber).start()

fun getDeviceProperty(serialNumber: String, property: String): String {
    return runCatching {
        ProcessBuilder("adb", "-s", serialNumber, "shell", property).start().inputStream
            .bufferedReader()
            .use { reader ->
                reader.readLine()
            }
    }.getOrElse {
        EMPTY_STRING
    }.trim()
}

fun getDevicePropertyList(serialNumber: String, property: String, startingItem: String? = null): List<String> {
    return runCatching {
        val process = ProcessBuilder("adb", "-s", serialNumber, "shell", property).start()
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
