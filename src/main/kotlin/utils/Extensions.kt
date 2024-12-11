package utils

import com.jskako.droidsense.generated.resources.Res
import com.jskako.droidsense.generated.resources.error_clipboard_message_empty_data
import com.jskako.droidsense.generated.resources.error_clipboard_message_failed
import com.jskako.droidsense.generated.resources.error_export_empty_data
import com.jskako.droidsense.generated.resources.error_export_empty_path
import com.jskako.droidsense.generated.resources.error_export_general
import com.jskako.droidsense.generated.resources.error_folder_not_found
import com.jskako.droidsense.generated.resources.string_placeholder
import com.jskako.droidsense.generated.resources.success_clipboard_message
import com.jskako.droidsense.generated.resources.success_export_general
import com.jskako.droidsense.generated.resources.success_open_success
import data.ArgsText
import notifications.ExportData
import notifications.InfoManagerData
import org.jetbrains.compose.resources.getString
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
            message = ArgsText(
                textResId = Res.string.error_clipboard_message_empty_data,
            ),
            color = darkRed
        )
    }

    return if (copyToClipboard(this)) {
        InfoManagerData(
            message = ArgsText(
                textResId = Res.string.success_clipboard_message
            )
        )
    } else {
        InfoManagerData(
            message = ArgsText(
                textResId = Res.string.error_clipboard_message_failed,
            ),
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

suspend fun String.exportToFile(
    exportPath: String? = null
): ExportData {

    if (this.isBlank()) {
        return ExportData(
            infoManagerData = InfoManagerData(
                message = ArgsText(
                    textResId = Res.string.error_export_empty_data,
                ),
                color = darkRed
            )
        )
    }

    val path = exportPath ?: pickDirectoryDialog()
    if (path.isNullOrBlank()) {
        return ExportData(
            infoManagerData = InfoManagerData(
                message = ArgsText(
                    textResId = Res.string.error_export_empty_path,
                ),
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
                message = ArgsText(
                    textResId = Res.string.success_export_general,
                    formatArgs = listOf(path)
                ),
                duration = null,
                buttonVisible = true
            ),
            path = path
        )
    }.getOrElse {
        ExportData(
            infoManagerData = InfoManagerData(
                message = ArgsText(
                    textResId = Res.string.error_export_general,
                ),
                color = darkRed
            )
        )
    }
}

suspend fun openFolderAtPath(path: String): InfoManagerData {
    val result = runCatching {
        val file = File(path)
        if (file.exists() && Desktop.isDesktopSupported()) {
            Desktop.getDesktop().open(file)
        } else {
            throw IllegalArgumentException("${getString(Res.string.error_folder_not_found)} - $path")
        }
    }

    return if (result.isFailure) {
        InfoManagerData(
            message = ArgsText(
                textResId = Res.string.string_placeholder,
                formatArgs = listOf(result.exceptionOrNull()?.message ?: "")
            ),
            color = darkRed
        )
    } else {
        InfoManagerData(
            message = ArgsText(
                textResId = Res.string.success_open_success,
                formatArgs = listOf(path)
            )
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