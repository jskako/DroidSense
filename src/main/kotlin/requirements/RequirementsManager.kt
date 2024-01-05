package requirements

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Checklist
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.Warning
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.vector.ImageVector
import java.io.File
import kotlin.Result.Companion.failure
import kotlin.Result.Companion.success
import kotlinx.coroutines.delay
import settitngs.GlobalVariables
import utils.ADB_WINDOWS_PATH
import utils.DEFAULT_DELAY
import utils.OS
import utils.SCRCPY_WINDOWS_PATH
import utils.getStringResource
import utils.getUserOS

class RequirementsManager(private val globalVariables: GlobalVariables) {

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
            // TODO - Remove globalVariables and use database
            OS.WINDOWS.osName() -> {
                globalVariables.setAdbPath(File(ADB_WINDOWS_PATH).absolutePath)
                globalVariables.setScrCpyPath(File(SCRCPY_WINDOWS_PATH).absolutePath)
                setSucceed()
                success(true)
            }

            OS.MAC.osName(), OS.LINUX.osName() -> {
                // TODO - Check if exist in database, if yes continue to main, else error
                return if (globalVariables.isValid) {
                    setSucceed()
                    success(true)
                } else {
                    setFailure(getStringResource("info.requirements.adb.error"))
                    failure(
                        exception = Throwable(
                            message = getStringResource("info.requirements.adb.error")
                        )
                    )
                }
            }

            else -> {
                // TODO - FIx failure to go on proper place
                getStringResource("info.requirements.os.error").let {
                    setFailure(it)
                    failure(Throwable(it))
                }
            }
        }
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