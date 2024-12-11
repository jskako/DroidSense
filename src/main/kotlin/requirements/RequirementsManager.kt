package requirements

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Android
import androidx.compose.material.icons.filled.Checklist
import androidx.compose.material.icons.filled.Hardware
import androidx.compose.material.icons.filled.Warning
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.vector.ImageVector
import com.jskako.droidsense.generated.resources.Res
import com.jskako.droidsense.generated.resources.info_requirements_adb_error
import com.jskako.droidsense.generated.resources.info_requirements_error
import com.jskako.droidsense.generated.resources.info_requirements_general
import com.jskako.droidsense.generated.resources.info_requirements_os_error
import com.jskako.droidsense.generated.resources.info_requirements_succeed
import com.jskako.droidsense.generated.resources.info_requirements_welcome
import data.keys.SettingsKey
import data.repository.settings.SettingsSource
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.getString
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
import utils.getUserOS
import java.io.File
import kotlin.Result.Companion.failure
import kotlin.Result.Companion.success

class RequirementsManager(
    private val settingsSource: SettingsSource,
) {

    private val _description = mutableStateOf("")
    private val _icon = mutableStateOf(Icons.Default.Android)

    val description: State<String>
        get() = _description

    val icon: State<ImageVector>
        get() = _icon

    suspend fun executeRequirements(): Result<Boolean> {
        delay(DEFAULT_DELAY)
        return if (settingsSource.isValid()) {
            setSucceed(messageRes = Res.string.info_requirements_welcome)
            success(true)
        } else {
            _icon.value = Icons.Default.Checklist
            _description.value = getString(Res.string.info_requirements_general)
            delay(DEFAULT_DELAY)
            when (getUserOS()) {
                OS.WINDOWS -> {
                    getOSArch().also {
                        when (it) {
                            Arch.BIT_32 -> {
                                setPath(
                                    listOf(
                                        Path(
                                            pathName = SettingsKey.ADB,
                                            path = File(ADB_WINDOWS_32_PATH).absolutePath
                                        ),
                                        Path(
                                            pathName = SettingsKey.SCRCPY,
                                            path = File(SCRCPY_WINDOWS_32_PATH).absolutePath
                                        )
                                    )
                                )
                            }

                            Arch.BIT_64 -> {
                                setPath(
                                    listOf(
                                        Path(
                                            pathName = SettingsKey.ADB,
                                            path = File(ADB_WINDOWS_64_PATH).absolutePath
                                        ),
                                        Path(
                                            pathName = SettingsKey.SCRCPY,
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
                                pathName = SettingsKey.ADB,
                                path = ADB_PACKAGE.findPath()
                            ),
                            Path(
                                pathName = SettingsKey.SCRCPY,
                                path = SCRCPY_PACKAGE.findPath()
                            )
                        )
                    )

                    return if (settingsSource.isValid()) {
                        setSucceed()
                        success(true)
                    } else {
                        setFailure(getString(Res.string.info_requirements_adb_error))
                        failure(
                            exception = Throwable(
                                message = getString(Res.string.info_requirements_adb_error)
                            )
                        )
                    }
                }

                else -> {
                    // TODO - FIx failure to go on proper place
                    getString(Res.string.info_requirements_os_error).let {
                        setFailure(it)
                        failure(Throwable(it))
                    }
                }
            }
        }
    }

    private suspend fun setPath(path: List<Path>) {
        path.forEach {
            when (it.pathName) {
                SettingsKey.ADB -> {
                    settingsSource.add(
                        identifier = SettingsKey.ADB.name,
                        value = it.path
                    )
                }

                SettingsKey.SCRCPY -> {
                    settingsSource.add(
                        identifier = SettingsKey.SCRCPY.name,
                        value = it.path
                    )
                }
            }
        }
    }

    private suspend fun setSucceed(messageRes: StringResource = Res.string.info_requirements_succeed) {
        _icon.value = Icons.Default.Hardware
        _description.value = getString(messageRes)
        delay(DEFAULT_DELAY)
    }

    private suspend fun setFailure(error: String?) {
        _icon.value = Icons.Default.Warning
        _description.value = error ?: getString(Res.string.info_requirements_error)
        delay(DEFAULT_DELAY)
    }
}

private data class Path(
    val pathName: SettingsKey,
    val path: String
)
