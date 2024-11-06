package data.repository.phone

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.jskako.PhoneInfoQueries
import data.model.items.PhoneItem
import data.model.mappers.toPhoneItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.coroutines.CoroutineContext

class PhoneSource(
    private val phoneDao: PhoneInfoQueries,
) : PhoneRepository {

    override suspend fun add(phoneItem: PhoneItem) = phoneDao.insert(
        serialNumber = phoneItem.serialNumber,
        model = phoneItem.model,
        manufacturer = phoneItem.manufacturer,
        brand = phoneItem.brand,
    )

    override fun by(serialNumber: String) = phoneDao.getPhoneInfoBy(serialNumber).executeAsOneOrNull()?.toPhoneItem()

    override fun by(context: CoroutineContext): Flow<List<PhoneItem>> =
        phoneDao.phones().asFlow().mapToList(context).map {
            it.map { phoneInfo ->
                phoneInfo.toPhoneItem()
            }
        }

    override suspend fun deleteBy(serialNumber: String) {
        phoneDao.deleteBy(serialNumber)
    }

    override suspend fun deleteAll() = phoneDao.nukeTable()

}