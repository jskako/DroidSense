package utils

import com.jskako.droidsense.generated.resources.Res
import com.jskako.droidsense.generated.resources.info_directory_general
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.getString
import java.awt.FileDialog
import java.awt.Frame
import java.io.File
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Locale

fun getTimeStamp(format: String = DEFAULT_TIMESTAMP): String = DateTimeFormatter
    .ofPattern(format)
    .withZone(ZoneOffset.UTC)
    .format(LocalDateTime.now())


suspend fun pickDirectoryDialog(): String? {
    val fileDialog = FileDialog(Frame(), getString(Res.string.info_directory_general), FileDialog.LOAD).apply {
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

suspend fun pickFile(
    titleRes: StringResource = Res.string.info_directory_general,
    allowedExtension: String? = null
): File? {
    val fileDialog = FileDialog(null as Frame?, getString(titleRes), FileDialog.LOAD)
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