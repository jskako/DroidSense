package adb.application

data class AppData(
    val packageId: String,
    val appPath: String?,
    val appSize: String?,
    val applicationType: ApplicationType
)