package utils

import di.AppModule.provideInfoManager
import di.AppModule.provideLogManager
import logs.LogDetails
import java.awt.Toolkit
import java.awt.datatransfer.Clipboard
import java.awt.datatransfer.StringSelection
import java.io.File
import java.io.FileWriter

private val infoManager = provideInfoManager()
private val logManager = provideLogManager()

fun String.copyToClipboard() {
    if (this.trim().isEmpty()) {
        infoManager.showInfoMessage(getStringResource("error.clipboard.message.empty.data"))
        return
    }

    if (copyToClipboard(this)) {
        infoManager.showInfoMessage(getStringResource("success.clipboard.message"))
    } else {
        infoManager.showInfoMessage(getStringResource("error.clipboard.message.failed"))
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
        infoManager.showInfoMessage(getStringResource("error.export.empty.data"))
        return
    }

    val path = exportPath ?: pickDirectoryDialog()
    if (path.isNullOrBlank()) {
        infoManager.showInfoMessage(getStringResource("error.export.empty.path"))
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
        logManager.addLog("${getStringResource("error.export.general")}: ${e.message}")
    }

    result.onSuccess {
        logManager.addLog("${getStringResource("success.export.general")}: $filePath", filePath)
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