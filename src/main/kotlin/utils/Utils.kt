package utils

import androidx.compose.ui.res.loadImageBitmap
import androidx.compose.ui.res.useResource
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.withContext
import notifications.InfoManagerData
import utils.Colors.darkRed
import java.awt.Desktop
import java.awt.FileDialog
import java.awt.Frame
import java.io.File
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Locale
import java.util.ResourceBundle

fun getResourceBundle(baseName: String): ResourceBundle = ResourceBundle.getBundle(baseName)
fun getStringResource(resourceName: String) =
    getResourceBundle(STRING_RESOURCES).getString(resourceName) ?: EMPTY_STRING

fun getTimeStamp(format: String = DEFAULT_TIMESTAMP): String = DateTimeFormatter
    .ofPattern(format)
    .withZone(ZoneOffset.UTC)
    .format(LocalDateTime.now())

fun openFile(path: String): InfoManagerData {
    val result = runCatching {
        val file = File(path)
        check(Desktop.isDesktopSupported()) { getStringResource("error.openfile.desktop.unsupported") }
        check(file.exists()) { getStringResource("error.openfile.no.file") }
        Desktop.getDesktop().open(file)
    }

    return if (result.isSuccess) {
        InfoManagerData(
            message = "${getStringResource("success.openfile.general")}: $path"
        )
    } else {
        InfoManagerData(
            message = "${getStringResource("error.openfile.cannot.open")} $path\n${result.exceptionOrNull()}"
        )
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

fun pickFile(
    title: String = getStringResource("info.open.file.default"),
    allowedExtension: String? = null
): File? {
    val fileDialog = FileDialog(null as Frame?, title, FileDialog.LOAD)
    fileDialog.isVisible = true
    fileDialog.isMultipleMode = false

    val selectedFile = File(fileDialog.directory, fileDialog.file)
    return selectedFile.takeIf { allowedExtension?.equals(it.extension, ignoreCase = true) != false }
}

fun getUserOS(): OS {
    val osName = System.getProperty(SYSTEM_OS_PROPERTY).lowercase(Locale.getDefault())
    return OS.entries.firstOrNull { osName.contains(it.osName(), ignoreCase = true) }
        ?: OS.UNSUPPORTED
}

fun getOSArch(): Arch {
    val osArch = System.getProperty(SYSTEM_OS_ARCH)
    return Arch.entries.firstOrNull { osArch.contains(it.archName(), ignoreCase = true) }
        ?: Arch.UNSUPPORTED
}

fun getImageBitmap(path: String) = useResource(path) { loadImageBitmap(it) }

fun startScrCpy(
    scrCpyPath: String,
    serialNumber: String
): Process =
    ProcessBuilder(scrCpyPath, "-s", serialNumber).start()

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

suspend fun installApplication(
    adbPath: String,
    identifier: String,
    onMessage: (InfoManagerData) -> Unit
) {
    return withContext(Default) {
        runCatching {
            pickFile(allowedExtension = APK_EXTENSION)?.let { file ->
                onMessage(
                    InfoManagerData(
                        message = "${getStringResource("info.file.install")}: ${file.name}",
                        duration = null
                    )
                )

                val command = listOf(adbPath, "-s", identifier, "install", "-r", file.absolutePath)
                val process = ProcessBuilder(command).apply {
                    redirectErrorStream(true)
                }.start()

                val exitCode = process.waitFor()

                if (exitCode == 0) {
                    onMessage(
                        InfoManagerData(
                            message = "${getStringResource("success.file.install")}: ${file.canonicalPath}"
                        )
                    )
                } else {
                    onMessage(
                        InfoManagerData(
                            message = "${getStringResource("error.file.install")}: ${file.canonicalPath}",
                            color = darkRed
                        )
                    )
                }
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
}

fun isValidIpAddressWithPort(input: String) = ipPortRegex.matches(input)