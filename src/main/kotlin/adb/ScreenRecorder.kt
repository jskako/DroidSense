package adb

import com.jskako.droidsense.generated.resources.Res
import com.jskako.droidsense.generated.resources.error_recording
import com.jskako.droidsense.generated.resources.error_recording_stopped
import com.jskako.droidsense.generated.resources.error_screenshot
import com.jskako.droidsense.generated.resources.error_screenshot_general
import com.jskako.droidsense.generated.resources.info_recording_started
import com.jskako.droidsense.generated.resources.info_recording_stopped
import com.jskako.droidsense.generated.resources.info_screenshot_saved
import data.ArgsText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import notifications.ExportData
import notifications.InfoManagerData
import org.jetbrains.compose.resources.getString
import utils.Colors.darkRed
import utils.runBinaryCommand
import utils.startLongRunningProcess
import kotlin.io.path.Path
import kotlin.io.path.writeBytes

class ScreenRecorder(
    private val identifier: String,
    private val onInfoMessage: (ExportData) -> Unit
) {

    private var screenProcess: Process? = null
    private var output: String? = null

    suspend fun startRecording(
        outputFilePath: String,
        scrCpyPath: String,
        adbPath: String
    ) {
        withContext(Dispatchers.IO) {
            runCatching {
                val command = "$scrCpyPath --serial $identifier --no-playback --no-window --record $outputFilePath"

                screenProcess = command.startLongRunningProcess(adbPath)
                output = outputFilePath

                onInfoMessage(
                    ExportData(
                        infoManagerData = InfoManagerData(
                            message = ArgsText(
                                textResId = Res.string.info_recording_started,
                                formatArgs = listOf(outputFilePath)
                            ),
                            duration = null
                        )
                    )
                )
            }.onFailure {
                onInfoMessage(
                    ExportData(
                        infoManagerData = InfoManagerData(
                            color = darkRed,
                            message = ArgsText(
                                textResId = Res.string.error_recording,
                                formatArgs = listOf(it.localizedMessage)
                            )
                        )
                    )
                )
            }
        }
    }

    suspend fun stopRecording() {
        withContext(Dispatchers.IO) {
            runCatching {
                screenProcess?.destroy()
                screenProcess = null
                onInfoMessage(
                    ExportData(
                        path = output,
                        infoManagerData = InfoManagerData(
                            message = ArgsText(
                                textResId = Res.string.info_recording_stopped,
                                formatArgs = listOf(output ?: "")
                            ),
                            buttonVisible = true,
                            duration = null
                        )
                    )
                )
            }.onFailure {
                onInfoMessage(
                    ExportData(
                        infoManagerData = InfoManagerData(
                            color = darkRed,
                            message = ArgsText(
                                textResId = Res.string.error_recording_stopped,
                                formatArgs = listOf(it.localizedMessage)
                            )
                        )
                    )
                )
            }
        }
    }

    suspend fun takeScreenshot(
        adbPath: String,
        outputFilePath: String
    ) {
        withContext(Dispatchers.IO) {
            runCatching {
                val command = "$adbPath -s $identifier exec-out screencap -p"
                val screenshotBytes =
                    command.runBinaryCommand() ?: throw Exception(getString(Res.string.error_screenshot_general))
                Path(outputFilePath).writeBytes(screenshotBytes)
                onInfoMessage(
                    ExportData(
                        path = outputFilePath,
                        infoManagerData = InfoManagerData(
                            message = ArgsText(
                                textResId = Res.string.info_screenshot_saved,
                                formatArgs = listOf(outputFilePath)
                            ),
                            buttonVisible = true,
                            duration = null
                        )
                    )
                )
            }.onFailure {
                onInfoMessage(
                    ExportData(
                        infoManagerData = InfoManagerData(
                            color = darkRed,
                            message = ArgsText(
                                textResId = Res.string.error_screenshot,
                                formatArgs = listOf(it.localizedMessage)
                            )
                        )
                    )
                )
            }
        }
    }
}
