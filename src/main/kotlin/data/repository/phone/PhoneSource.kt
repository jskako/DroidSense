package data.repository.phone

import com.jskako.PhoneInfoQueries
import data.model.items.PhoneItem
import data.model.mappers.toPhoneItem

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

    override suspend fun deleteBy(serialNumber: String) {
        phoneDao.deleteBy(serialNumber)
    }

    override suspend fun deleteAll() = phoneDao.nukeTable()

}