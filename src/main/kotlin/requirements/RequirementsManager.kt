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
import utils.ADB_PACKAGE
import utils.ADB_WINDOWS_32_PATH
import utils.ADB_WINDOWS_64_PATH
import utils.Arch
import utils.DEFAULT_DELAY
import utils.OS
import utils.SCRCPY_PACKAGE
import utils.SCRCPY_WINDOWS_32_PATH
import utils.SCRCPY_WINDOWS_64_PATH
import utils.findPath
import utils.getOSArch
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
            OS.WINDOWS -> {
                getOSArch().also {
                    when (it) {
                        Arch.BIT_32 -> {
                            setPath(
                                listOf(
                                    Path(
                                        pathName = PathName.ADB,
                                        path = File(ADB_WINDOWS_32_PATH).absolutePath
                                    ),
                                    Path(
                                        pathName = PathName.SCRCPY,
                                        path = File(SCRCPY_WINDOWS_32_PATH).absolutePath
                                    )
                                )
                            )
                        }

                        Arch.BIT_64 -> {
                            setPath(
                                listOf(
                                    Path(
                                        pathName = PathName.ADB,
                                        path = File(ADB_WINDOWS_64_PATH).absolutePath
                                    ),
                                    Path(
                                        pathName = PathName.SCRCPY,
                                        path = File(SCRCPY_WINDOWS_64_PATH).absolutePath
                                    )
                                )
                            )
                        }

                        Arch.UNSUPPORTED -> TODO()
                    }
                }
                setSucceed()
                success(true)
            }

            OS.MAC, OS.LINUX -> {
                setPath(
                    listOf(
                        Path(
                            pathName = PathName.ADB,
                            path = ADB_PACKAGE.findPath()
                        ),
                        Path(
                            pathName = PathName.SCRCPY,
                            path = SCRCPY_PACKAGE.findPath()
                        )
                    )
                )
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

    private fun setPath(path: List<Path>) {
        // TODO - Remove globalVariables and use database
        path.forEach {
            when (it.pathName) {
                PathName.ADB -> {
                    globalVariables.setAdbPath(it.path)
                }

                PathName.SCRCPY -> {
                    globalVariables.setScrCpyPath(it.path)
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

private data class Path(
    val pathName: PathName,
    val path: String
)


private enum class PathName {
    ADB, SCRCPY
}