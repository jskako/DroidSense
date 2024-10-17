package log

import adb.ApplicationType
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
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
                val apps = mutableListOf<Deferred<AppData?>>()

                appProcess.inputStream.bufferedReader().useLines { lines ->
                    lines.forEach { packageId ->
                        packageId.replace("package:", "").let {
                            if (it.trim().isNotEmpty()) {
                                apps.add(async {
                                    val appPath = getAppPath(adbPath, it)
                                    val appSize = getAppSize(adbPath, appPath)
                                    if (appPath != null && appSize != null) {
                                        AppData(
                                            packageId = it,
                                            appPath = appPath,
                                            appSize = appSize,
                                            applicationType = applicationType
                                        )
                                    } else {
                                        null
                                    }
                                })
                            }
                        }
                    }
                }

                onDone(apps.awaitAll().filterNotNull())
            }.onFailure {
                onDone(emptyList())
            }
        }
    }

    private suspend fun getAppPath(
        adbPath: String,
        packageId: String
    ): String? = withContext(Dispatchers.IO) {
        val process = ProcessBuilder(adbPath, "shell", "pm", "path", packageId).start()

        return@withContext runCatching {
            process.inputStream.bufferedReader().useLines { lines ->
                lines.firstNotNullOfOrNull { line ->
                    if (line.contains("package:")) line.replace("package:", "") else null
                }
            }
        }.getOrNull()
    }

    private suspend fun getAppSize(
        adbPath: String,
        apkPath: String?
    ): String? = apkPath?.let {
        withContext(Dispatchers.IO) {
            val process = ProcessBuilder(adbPath, "shell", "du", "-h", apkPath).start()

            return@withContext runCatching {
                process.inputStream.bufferedReader().useLines { lines ->
                    lines.firstNotNullOfOrNull { line ->
                        line.split(" ").firstOrNull()?.split("\\s+".toRegex())?.first()
                    }
                }
            }.getOrNull()
        }
    }
}