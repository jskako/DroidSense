package data.repository.phone

import data.model.items.PhoneItem

interface PhoneRepository {

    suspend fun add(phoneItem: PhoneItem)
    fun by(serialNumber: String): PhoneItem?
    suspend fun deleteBy(serialNumber: String)
    suspend fun deleteAll()
}