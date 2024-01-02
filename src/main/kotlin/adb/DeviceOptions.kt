package adb

data class DeviceOptions(
    val text: String,
    val function: () -> Unit,
    val enabled: Boolean = true
)
