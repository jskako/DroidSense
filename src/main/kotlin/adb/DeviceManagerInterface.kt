package adb

import notifications.InfoManagerData

interface DeviceManagerInterface {
    suspend fun updateDevicesStatus(status: MonitoringStatus = MonitoringStatus.NOT_MONITORING)
    suspend fun addDevice(
        serialNumber: String,
        onMessage: (InfoManagerData) -> Unit
    )

    suspend fun removeDevice(
        serialNumber: String,
        onMessage: (InfoManagerData) -> Unit
    )

    suspend fun getDevicePackages(serialNumber: String): String
    fun clearDevices()
}