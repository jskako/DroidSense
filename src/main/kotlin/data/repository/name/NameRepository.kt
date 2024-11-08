package data.repository.name

import data.model.items.NameItem
import kotlinx.coroutines.flow.Flow
import java.util.UUID
import kotlin.coroutines.CoroutineContext

interface NameRepository {

    suspend fun add(nameItem: NameItem)
    fun by(context: CoroutineContext): Flow<List<NameItem>>
    fun by(sessionUuid: UUID): NameItem?
    fun uuids(serialNumber: String): List<UUID>
    fun update(sessionUuid: UUID, name: String)
    suspend fun deleteBy(sessionUuid: UUID)
    suspend fun deleteAll()
}