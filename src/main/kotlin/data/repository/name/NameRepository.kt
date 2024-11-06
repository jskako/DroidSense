package data.repository.name

import data.model.items.NameItem
import kotlinx.coroutines.flow.Flow
import java.util.UUID
import kotlin.coroutines.CoroutineContext

interface NameRepository {

    suspend fun add(nameItem: NameItem)
    fun by(context: CoroutineContext): Flow<List<NameItem>>
    fun by(uuid: UUID): NameItem?
    fun uuids(serialNumber: String): List<UUID>
    fun update(uuid: UUID, name: String)
    suspend fun deleteBy(uuid: UUID)
    suspend fun deleteAll()
}