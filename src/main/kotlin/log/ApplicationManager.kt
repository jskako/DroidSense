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
        identifier: String
    ) = withContext(Dispatchers.IO) {
        val applicationTypeIdentifier = when (applicationType) {
            ApplicationType.SYSTEM -> "s"
            ApplicationType.USER -> "3"
        }

        val appsProcess = runProcess(
            adbPath,
            "-s", identifier,
            "shell", "pm", "list", "packages", "-$applicationTypeIdentifier"
        )

        return@withContext runCatching {
            val apps = mutableListOf<Deferred<AppData?>>()

            appsProcess?.inputStream?.bufferedReader()?.useLines { lines ->
                lines.forEach { packageId ->
                    val cleanPackageId = packageId.replace("package:", "").trim()
                    if (cleanPackageId.isNotEmpty()) {
                        apps.add(async(Dispatchers.IO) {
                            val appPath = getAppPath(cleanPackageId)
                            val appSize = getAppSize(appPath)
                            if (appPath != null && appSize != null) {
                                AppData(
                                    packageId = cleanPackageId,
                                    appPath = appPath,
                                    appSize = appSize,
                                    applicationType = applicationType
                                )
                            } else null
                        })
                    }
                }
            }
            apps.awaitAll().filterNotNull()
        }.getOrElse {
            emptyList()
        }
    }

    private suspend fun getAppPath(packageId: String): String? = withContext(Dispatchers.IO) {
        val process = runProcess(adbPath, "shell", "pm", "path", packageId)
        return@withContext parseProcessOutput(process) { line ->
            if (line.contains("package:")) line.replace("package:", "") else null
        }
    }

    private suspend fun getAppSize(apkPath: String?): String? = apkPath?.let {
        withContext(Dispatchers.IO) {
            val process = runProcess(adbPath, "shell", "du", "-h", apkPath)
            return@withContext parseProcessOutput(process) { line ->
                line.split("\\s+".toRegex()).firstOrNull()
            }
        }
    }

    private fun runProcess(vararg args: String): Process? = runCatching {
        ProcessBuilder(*args).start()
    }.getOrNull()

    private fun parseProcessOutput(
        process: Process?,
        parseLine: (String) -> String?
    ): String? {
        return process?.inputStream?.bufferedReader()?.useLines { lines ->
            lines.firstNotNullOfOrNull { line -> parseLine(line) }
        }
    }
}
