package utils

import com.jskako.droidsense.generated.resources.Res
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.compose.resources.ExperimentalResourceApi
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

suspend fun shareScreen(
    scrCpyPath: String,
    identifier: String,
    adbPath: String
): Result<Unit> = withContext(Dispatchers.IO) {
    runCatching {
        ProcessBuilder(scrCpyPath, "-s", identifier)
            .redirectErrorStream(true)
            .apply {
                environment()["ADB"] = adbPath
            }
            .start()
        Result.success(Unit)
    }.getOrElse { Result.failure(it) }
}

fun isValidIpAddressWithPort(input: String) = ipPortRegex.matches(input)

fun getSpaceId(userInfo: String) = spaceIdRegex.find(userInfo)?.groups?.get(1)?.value?.toInt()

@OptIn(ExperimentalResourceApi::class)
suspend fun readFile(path: String) = Res.readBytes(path).decodeToString()