package requirements

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Warning
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.vector.ImageVector
import kotlinx.coroutines.delay
import settitngs.GlobalSettings.setAdbPath
import settitngs.GlobalSettings.setScrCpyPath
import utils.ADB_PACKAGE
import utils.ADB_WINDOWS_PATH
import utils.DEFAULT_DELAY
import utils.OS
import utils.SCRCPY_PACKAGE
import utils.SCRCPY_WINDOWS_PATH
import utils.getStringResource
import utils.getUserOS
import utils.isSoftwareInstalled
import java.io.File
import kotlin.Result.Companion.failure
import kotlin.Result.Companion.success

object RequirementsManager {

    private val _description = mutableStateOf(DEFAULT_DESCRIPTION)
    private val _icon = mutableStateOf(DEFAULT_ICON)

    val description: State<String>
        get() = _description

    val icon: State<ImageVector>
        get() = _icon

    suspend fun executeRequirements(): Result<Boolean> {
        delay(DEFAULT_DELAY)
        return when (getUserOS()) {
            OS.WINDOWS.osName() -> {
                setAdbPath(File(ADB_WINDOWS_PATH).absolutePath)
                setScrCpyPath(File(SCRCPY_WINDOWS_PATH).absolutePath)
                setSucceed()
                success(true)
            }

            OS.MAC.osName(), OS.LINUX.osName() -> {
                macLinuxRequirements()
            }

            else -> {
                getStringResource("info.requirements.os.error").let {
                    setFailure(it)
                    failure(Throwable(it))
                }
            }
        }
    }

    private suspend fun macLinuxRequirements(): Result<Boolean> {
        for (requirementsDetail in requirementsList) {
            with(requirementsDetail) {
                _icon.value = icon
                _description.value = description
                delay(DEFAULT_DELAY)

                if (!function()) {
                    setFailure(descriptionError)
                    return failure(Throwable(_description.value))
                }
            }
        }
        setSucceed()

        return success(true)
    }

    private suspend fun setSucceed() {
        _icon.value = Icons.Default.Done
        _description.value = getStringResource("info.requirements.succeed")
        delay(DEFAULT_DELAY)
    }

    private suspend fun setFailure(error: String?) {
        _icon.value = Icons.Default.Warning
        _description.value = error ?: DEFAULT_ERROR
        delay(DEFAULT_DELAY)
    }
}

private val requirementsList = listOf(
    RequirementsDetails(
        description = getStringResource("info.requirements.adb.general"),
        icon = Icons.Default.Build,
        function = { isSoftwareInstalled(ADB_PACKAGE) },
        descriptionError = getStringResource("info.requirements.adb.error")
    ),
    RequirementsDetails(
        description = getStringResource("info.requirements.scrcpy.general"),
        icon = Icons.Default.List,
        function = { isSoftwareInstalled(SCRCPY_PACKAGE) },
        descriptionError = getStringResource("info.requirements.scrcpy.error")
    )
)

private val DEFAULT_DESCRIPTION = getStringResource("info.requirements.general")
private val DEFAULT_ERROR = getStringResource("info.requirements.error")
private val DEFAULT_ICON = Icons.Default.Face