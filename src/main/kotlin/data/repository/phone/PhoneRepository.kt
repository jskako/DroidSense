package data.repository.phone

import data.model.items.PhoneItem
import kotlinx.coroutines.flow.Flow
import kotlin.coroutines.CoroutineContext

interface PhoneRepository {

    suspend fun add(phoneItem: PhoneItem)
    fun by(serialNumber: String): PhoneItem?
    fun by(context: CoroutineContext, serialNumber: String): Flow<List<PhoneItem>>
    suspend fun deleteBy(serialNumber: String)
    suspend fun deleteAll()
}