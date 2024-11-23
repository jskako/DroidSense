package data.repository.device

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.jskako.DeviceInfoQueries
import data.model.items.DeviceItem
import data.model.mappers.toDeviceItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.coroutines.CoroutineContext

class DeviceSource(
    private val deviceDao: DeviceInfoQueries,
) : DeviceRepository {

    override suspend fun add(deviceItem: DeviceItem) = deviceDao.insert(
        serialNumber = deviceItem.serialNumber,
        model = deviceItem.model,
        manufacturer = deviceItem.manufacturer,
        brand = deviceItem.brand,
    )

    override fun by(serialNumber: String) = deviceDao.getDeviceInfoBy(serialNumber).executeAsOneOrNull()?.toDeviceItem()

    override fun by(context: CoroutineContext): Flow<List<DeviceItem>> =
        deviceDao.devices().asFlow().mapToList(context).map {
            it.map { deviceInfo ->
                deviceInfo.toDeviceItem()
            }
        }

    override suspend fun deleteBy(serialNumber: String) {
        deviceDao.deleteBy(serialNumber)
    }

    override suspend fun deleteAll() = deviceDao.nukeTable()

}