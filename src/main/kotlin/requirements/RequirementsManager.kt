package requirements

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Android
import androidx.compose.material.icons.filled.Checklist
import androidx.compose.material.icons.filled.Hardware
import androidx.compose.material.icons.filled.Warning
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.vector.ImageVector
import data.keys.SettingsKey
import data.repository.settings.SettingsSource
import java.io.File
import kotlin.Result.Companion.failure
import kotlin.Result.Companion.success
import kotlinx.coroutines.delay
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

class RequirementsManager(
    private val settingsSource: SettingsSource,
) {

    private val defaultError = getStringResource("info.requirements.error")
    private val _description = mutableStateOf("")
    private val _icon = mutableStateOf(Icons.Default.Android)

    val description: State<String>
        get() = _description

    val icon: State<ImageVector>
        get() = _icon

    suspend fun executeRequirements(): Result<Boolean> {
        delay(DEFAULT_DELAY)
        return if (settingsSource.isValid()) {
            setSucceed(message = getStringResource("info.requirements.welcome"))
            success(true)
        } else {
            _icon.value = Icons.Default.Checklist
            _description.value = getStringResource("info.requirements.general")
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

    private suspend fun setSucceed(message: String = getStringResource("info.requirements.succeed")) {
        _icon.value = Icons.Default.Hardware
        _description.value = message
        delay(DEFAULT_DELAY)
    }

    private suspend fun setFailure(error: String?) {
        _icon.value = Icons.Default.Warning
        _description.value = error ?: defaultError
        delay(DEFAULT_DELAY)
    }
}

private data class Path(
    val pathName: SettingsKey,
    val path: String
)
