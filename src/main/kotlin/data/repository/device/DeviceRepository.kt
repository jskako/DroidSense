package data.repository.device

import data.model.items.DeviceItem
import kotlinx.coroutines.flow.Flow
import kotlin.coroutines.CoroutineContext

interface DeviceRepository {

    suspend fun add(deviceItem: DeviceItem)
    fun by(serialNumber: String): DeviceItem?
    fun by(context: CoroutineContext): Flow<List<DeviceItem>>
    suspend fun deleteBy(serialNumber: String)
    suspend fun deleteAll()
}