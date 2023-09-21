package utils

import notifications.InfoManager.showInfoMessage
import notifications.LogDetails
import notifications.LogManager.addLog
import java.awt.Toolkit
import java.awt.datatransfer.Clipboard
import java.awt.datatransfer.StringSelection
import java.io.File
import java.io.FileWriter

fun String.copyToClipboard() {
    if (this.trim().isEmpty()) {
        showInfoMessage(getStringResource("error.clipboard.message.empty.data"))
        return
    }

    if (copyToClipboard(this)) {
        showInfoMessage(getStringResource("success.clipboard.message"))
    } else {
        showInfoMessage(getStringResource("error.clipboard.message.failed"))
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
        showInfoMessage(getStringResource("error.export.empty.data"))
        return
    }

    val path = exportPath ?: pickDirectoryDialog()
    if (path.isNullOrBlank()) {
        showInfoMessage(getStringResource("error.export.empty.path"))
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
        addLog("${getStringResource("error.export.general")}: ${e.message}")
    }

    result.onSuccess {
        addLog("${getStringResource("success.export.general")}: $filePath", filePath)
    }
}

fun List<LogDetails>.prepareLogs() = buildString {
    for (log in this@prepareLogs) {
        appendLine(log.time)
        appendLine()
        appendLine(log.log)
        appendLine(emptyLine(1))
    }
}.trim()