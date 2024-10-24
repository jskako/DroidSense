package log

import adb.ApplicationType
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext
import notifications.InfoManagerData
import utils.APK_EXTENSION
import utils.getStringResource
import utils.pickFile

class ApplicationManager(
    private val adbPath: String,
    private val identifier: String
) {

    suspend fun getAppDetails(
        packageName: String
    ): List<String>? = withContext(Dispatchers.IO) {
        fun runProcess(vararg args: String): Process? = runCatching {
            ProcessBuilder(*args).start()
        }.getOrNull()

        fun parseProcessOutput(process: Process?): List<String>? {
            return process?.inputStream?.bufferedReader()?.useLines { lines ->
                val output = buildString {
                    lines.filter { it.isNotBlank() }.forEach { line ->
                        append(line).append("\n")
                    }
                }
                if (output.isNotEmpty()) listOf(output) else null
            }
        }

        val process = runProcess(adbPath, "-s", identifier, "shell", "dumpsys", "package", packageName)

        parseProcessOutput(process)
    }

    suspend fun uninstallApp(
        packageName: String
    ): Result<Unit> = withContext(Dispatchers.IO) {
        runCatching {
            val process = ProcessBuilder(adbPath, "-s", identifier, "shell", "pm", "uninstall", packageName).start()
            val isSuccess = process.inputStream.bufferedReader().useLines { lines ->
                lines.any { it.contains("Success") }
            }

            if (isSuccess) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Failed to uninstall the app"))
            }
        }.getOrElse { Result.failure(it) }
    }

    suspend fun clearAppCache(
        packageName: String
    ): Result<Unit> = withContext(Dispatchers.IO) {
        runCatching {
            val process = ProcessBuilder(adbPath, "-s", identifier, "shell", "pm", "clear", packageName).start()
            val isSuccess = process.inputStream.bufferedReader().useLines { lines ->
                lines.any { it.contains("Success") }
            }

            if (isSuccess) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Failed to clear the app cache"))
            }
        }.getOrElse { Result.failure(it) }
    }

    suspend fun installApplication(
        spaceId: String
    ): Result<InfoManagerData> = withContext(Dispatchers.Default) {
        runCatching {
            val file = pickFile(allowedExtension = APK_EXTENSION) ?: return@withContext Result.failure(
                IllegalArgumentException("File selection was canceled.")
            )

            val command = listOf(adbPath, "-s", identifier, "install", "--user", spaceId, "-r", file.absolutePath)

            val process = ProcessBuilder(command).apply { redirectErrorStream(true) }.start()
            val exitCode = process.waitFor()

            val resultMessage = if (exitCode == 0) {
                getStringResource("success.file.install")
            } else {
                throw Exception(getStringResource("error.file.install"))
            }

            Result.success(
                InfoManagerData(
                    message = "$resultMessage: ${file.canonicalPath}"
                )
            )
        }.getOrElse {
            Result.failure(Exception(getStringResource("error.file.install")))
        }
    }


    private fun buildAdbInstallCommand(
        filePath: String,
        spaceId: String
    ): List<String> {
        return mutableListOf(adbPath, "-s", identifier, "install").apply {
            add("--user")
            add(spaceId)

            addAll(listOf("-r", filePath))
        }
    }

    suspend fun getAppsData(
        applicationType: ApplicationType
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
