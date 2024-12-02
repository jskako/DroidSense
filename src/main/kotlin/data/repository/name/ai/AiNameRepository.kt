package data.repository.name.ai

import data.model.items.AiNameItem
import kotlinx.coroutines.flow.Flow
import java.util.UUID
import kotlin.coroutines.CoroutineContext

interface AiNameRepository {

    suspend fun add(aiNameItem: AiNameItem)
    fun by(context: CoroutineContext): Flow<List<AiNameItem>>
    fun by(sessionUuid: UUID): AiNameItem?
    fun uuids(deviceSerialNumber: String): List<UUID>
    fun update(sessionUuid: UUID, name: String)
    suspend fun deleteBy(sessionUuid: UUID)
    suspend fun deleteBy(deviceSerialNumber: String)
    suspend fun deleteAll()
}