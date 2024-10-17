package log

import adb.ApplicationType

data class AppData(
    val packageId: String,
    val appPath: String?,
    val appSize: String?,
    val applicationType: ApplicationType
)