package log

import adb.ApplicationType
import androidx.compose.runtime.mutableStateListOf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ApplicationManager(
    private val adbPath: String
) {

    suspend fun getAppsData(
        applicationType: ApplicationType,
        identifier: String,
        onDone: (List<AppData>) -> Unit
    ) {

        withContext(Dispatchers.IO) {

            val applicationTypeIdentifier = when (applicationType) {
                ApplicationType.SYSTEM -> "s"
                ApplicationType.USER -> "3"
            }

            val appProcess = ProcessBuilder(
                adbPath,
                "-s",
                identifier,
                "shell",
                "pm",
                "list",
                "packages",
                "-$applicationTypeIdentifier"
            ).start()

            runCatching {
                val apps = mutableStateListOf<AppData>()
                appProcess.inputStream.bufferedReader().useLines { lines ->
                    lines.forEach { packageId ->
                        packageId.replace("package:", "").let {
                            if (it.trim().isNotEmpty()) {
                                val appPath = getAppPath(
                                    adbPath = adbPath,
                                    packageId = it
                                )
                                apps.add(
                                    AppData(
                                        packageId = it,
                                        appPath = appPath,
                                        appSize = getAppSize(
                                            adbPath = adbPath,
                                            apkPath = appPath
                                        ),
                                        applicationType = applicationType
                                    )
                                )
                            }
                        }
                    }
                }

                onDone(apps)

            }.onFailure { exception ->
                onDone(emptyList())
            }
        }
    }

    private fun clearAppCache(
        adbPath: String,
        packageId: String
    ) {
        runCatching {
            ProcessBuilder(adbPath, "shell", "pm", "clear", packageId).start()
        }.onFailure {

        }
    }

    private fun getAppPath(
        adbPath: String,
        packageId: String
    ): String? {
        val process = ProcessBuilder(adbPath, "shell", "pm", "path", packageId).start()

        return runCatching {
            process.inputStream.bufferedReader().useLines { lines ->
                lines.firstNotNullOfOrNull { line ->
                    if (line.contains("package:")) {
                        line.replace("package:", "")
                    } else null
                }
            }
        }.getOrNull()
    }

    private fun getAppSize(
        adbPath: String,
        apkPath: String?
    ): String? {
        return apkPath?.let {

            val process = ProcessBuilder(adbPath, "shell", "du", "-h", apkPath).start()

            return runCatching {
                process.inputStream.bufferedReader().useLines { lines ->
                    lines.firstNotNullOfOrNull { line ->
                        line.split(" ").firstOrNull()?.split("\\s+".toRegex())?.first()
                    }
                }
            }.getOrNull()
        }
    }
}