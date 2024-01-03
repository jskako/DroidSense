package requirements

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Adb
import androidx.compose.material.icons.filled.Checklist
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.ScreenShare
import androidx.compose.material.icons.filled.Warning
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.vector.ImageVector
import java.io.File
import kotlin.Result.Companion.failure
import kotlin.Result.Companion.success
import kotlinx.coroutines.delay
import settitngs.GlobalVariables
import utils.ADB_PACKAGE
import utils.ADB_WINDOWS_PATH
import utils.DEFAULT_DELAY
import utils.OS
import utils.SCRCPY_PACKAGE
import utils.SCRCPY_WINDOWS_PATH
import utils.findPath
import utils.getStringResource
import utils.getUserOS

class RequirementsManager(private val globalVariables: GlobalVariables) {

    private val requirementsList = listOf(
        RequirementsDetails(
            description = getStringResource("info.requirements.adb.general"),
            icon = Icons.Default.Adb,
            function = {
                ADB_PACKAGE.findPath().let {
                    if (it.isNotEmpty()) {
                        globalVariables.setAdbPath(it)
                        success(true)
                    } else failure(
                        exception = Throwable(
                            message = getStringResource("info.requirements.adb.error")
                        )
                    )
                }
            }
        ),
        RequirementsDetails(
            description = getStringResource("info.requirements.scrcpy.general"),
            icon = Icons.Default.ScreenShare,
            function = {
                SCRCPY_PACKAGE.findPath().let {
                    if (it.isNotEmpty()) {
                        globalVariables.setScrCpyPath(it)
                        success(true)
                    } else failure(
                        exception = Throwable(
                            message = getStringResource("info.requirements.scrcpy.error")
                        )
                    )
                }
            }
        )
    )

    private val defaultDescription = getStringResource("info.requirements.general")
    private val defaultError = getStringResource("info.requirements.error")
    private val defaultIcon = Icons.Default.Checklist
    private val _description = mutableStateOf(defaultDescription)
    private val _icon = mutableStateOf(defaultIcon)

    val description: State<String>
        get() = _description

    val icon: State<ImageVector>
        get() = _icon

    suspend fun executeRequirements(): Result<Boolean> {
        delay(DEFAULT_DELAY)
        return when (getUserOS()) {
            OS.WINDOWS.osName() -> {
                globalVariables.setAdbPath(File(ADB_WINDOWS_PATH).absolutePath)
                globalVariables.setScrCpyPath(File(SCRCPY_WINDOWS_PATH).absolutePath)
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
                function().fold(
                    onSuccess = {},
                    onFailure = {
                        setFailure(it.message)
                        return failure(Throwable(it.message))
                    }
                )
            }
        }
        setSucceed()
        return success(true)
    }

    private suspend fun setSucceed() {
        _icon.value = Icons.Default.DoneAll
        _description.value = getStringResource("info.requirements.succeed")
        delay(DEFAULT_DELAY)
    }

    private suspend fun setFailure(error: String?) {
        _icon.value = Icons.Default.Warning
        _description.value = error ?: defaultError
        delay(DEFAULT_DELAY)
    }
}