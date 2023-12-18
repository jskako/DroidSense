package utils

import notifications.InfoManager.showTimeLimitedInfoMessage
import java.awt.Toolkit
import java.awt.datatransfer.Clipboard
import java.awt.datatransfer.StringSelection
import java.io.File
import java.io.FileWriter

fun String.copyToClipboard() {
    if (this.trim().isEmpty()) {
        showTimeLimitedInfoMessage(getStringResource("error.clipboard.message.empty.data"))
        return
    }

    if (copyToClipboard(this)) {
        showTimeLimitedInfoMessage(getStringResource("success.clipboard.message"))
    } else {
        showTimeLimitedInfoMessage(getStringResource("error.clipboard.message.failed"))
    }
}

private fun copyToClipboard(text: String): Boolean {
    return runCatching {
        val clipboard: Clipboard = Toolkit.getDefaultToolkit().systemClipboard
        clipboard.setContents(StringSelection(text), null)
    }.isSuccess
}

fun String.exportToFile(exportPath: String? = null) {
    if (this.isBlank()) {
        showTimeLimitedInfoMessage(getStringResource("error.export.empty.data"))
        return
    }

    val path = exportPath ?: pickDirectoryDialog()
    if (path.isNullOrBlank()) {
        showTimeLimitedInfoMessage(getStringResource("error.export.empty.path"))
        return
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

    result.onFailure { e ->
        showTimeLimitedInfoMessage("${getStringResource("error.export.general")}: ${e.message}")
    }

    result.onSuccess {
        showTimeLimitedInfoMessage("${getStringResource("success.export.general")}: $filePath")
    }
}

fun String.capitalizeFirstChar() = replaceFirstChar(Char::titlecase)

fun String.runCommand(): String? = try {
    Runtime.getRuntime().exec(this).inputStream.bufferedReader().use { it.readText() }
} catch (e: Exception) {
    null
}
