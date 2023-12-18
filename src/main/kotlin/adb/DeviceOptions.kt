package adb

data class DeviceOptions(
    val text: String,
    val function: () -> Unit
)
