package utils

import notifications.ExportData
import notifications.InfoManagerData
import utils.Colors.darkRed
import java.awt.Desktop
import java.awt.Toolkit
import java.awt.datatransfer.Clipboard
import java.awt.datatransfer.StringSelection
import java.io.File
import java.io.FileWriter

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

fun String.exportToFile(
    exportPath: String? = null
): ExportData {

    if (this.isBlank()) {
        return ExportData(
            infoManagerData = InfoManagerData(
                message = getStringResource("error.export.empty.data"),
                color = darkRed
            )
        )
    }

    val path = exportPath ?: pickDirectoryDialog()
    if (path.isNullOrBlank()) {
        return ExportData(
            infoManagerData = InfoManagerData(
                message = getStringResource("error.export.empty.path"),
                color = darkRed
            )
        )
    }

    return runCatching {
        val file = File(path)
        file.parentFile?.mkdirs()
        FileWriter(file, false).use { writer ->
            writer.write(this)
        }
        ExportData(
            infoManagerData = InfoManagerData(
                message = "${getStringResource("success.export.general")}: $path",
                duration = null,
                buttonVisible = true
            ),
            path = path
        )
    }.getOrElse {
        ExportData(
            infoManagerData = InfoManagerData(
                message = getStringResource("error.export.general"),
                color = darkRed
            )
        )
    }
}
fun openFolderAtPath(path: String): InfoManagerData {
    val result = runCatching {
        val file = File(path)
        if (file.exists() && Desktop.isDesktopSupported()) {
            Desktop.getDesktop().open(file)
        } else {
            throw IllegalArgumentException("${getStringResource("error.folder.not.found")} - $path")
        }
    }

    return if (result.isFailure) {
        InfoManagerData(
            message = result.exceptionOrNull()?.message ?: "",
            color = darkRed
        )
    } else {
        InfoManagerData(
            message = "${getStringResource("success.open.success")}: $path"
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

fun String.findPath(): String = "which $this".runCommand()?.trim() ?: ""