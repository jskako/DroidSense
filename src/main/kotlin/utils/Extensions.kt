package utils

import java.awt.Toolkit
import java.awt.datatransfer.Clipboard
import java.awt.datatransfer.StringSelection
import java.io.File
import java.io.FileWriter
import notifications.InfoManagerData
import utils.Colors.darkRed

fun String.copyToClipboard(): InfoManagerData {
    if (this.trim().isEmpty()) {
        return InfoManagerData(
            message = getStringResource("error.clipboard.message.empty.data"),
            color = darkRed
        )
    }

    return if (copyToClipboard(this)) {
        InfoManagerData(
            message = getStringResource("success.clipboard.message")
        )
    } else {
        InfoManagerData(
            message = getStringResource("error.clipboard.message.failed"),
            color = darkRed
        )
    }
}

private fun copyToClipboard(text: String): Boolean {
    return runCatching {
        val clipboard: Clipboard = Toolkit.getDefaultToolkit().systemClipboard
        clipboard.setContents(StringSelection(text), null)
    }.isSuccess
}

fun String.exportToFile(exportPath: String? = null): InfoManagerData {
    if (this.isBlank()) {
        return InfoManagerData(
            message = getStringResource("error.export.empty.data"),
            color = darkRed
        )
    }

    val path = exportPath ?: pickDirectoryDialog()
    if (path.isNullOrBlank()) {
        return InfoManagerData(
            message = getStringResource("error.export.empty.path"),
            color = darkRed
        )
    }

    val timestamp = getTimeStamp(EXPORT_DATA_TIMESTAMP)
    val filePath = exportPath?.takeIf { it.isNotEmpty() } ?: "$path-$timestamp$LOG_EXTENSION"

    val result = runCatching {
        val file = File(filePath)
        file.parentFile?.mkdirs()
        FileWriter(file, false).use { writer ->
            writer.write(this)
        }
    }

    return if (result.isFailure) {
        InfoManagerData(
            message = getStringResource("error.export.general"),
            color = darkRed
        )
    } else {
        InfoManagerData(
            message = getStringResource("${getStringResource("success.export.general")}: $filePath"),
            duration = null
        )
    }
}

fun String.capitalizeFirstChar() = replaceFirstChar(Char::titlecase)

fun String.runCommand(): String? = runCatching {
    ProcessBuilder(this.split("\\s".toRegex()))
        .redirectErrorStream(true)
        .start()
        .inputStream.bufferedReader().use { it.readText().trim() }
}.getOrNull()