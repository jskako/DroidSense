package adb

interface DeviceManagerInterface {
    suspend fun updateDevicesStatus(status: AdbDeviceStatus = AdbDeviceStatus.NOT_MONITORING)
    suspend fun addDevice(serialNumber: String)
    suspend fun removeDevice(serialNumber: String)
    suspend fun getDevicePackages(serialNumber: String): String
    fun clearDevices()
}