package data.repository.ai

import data.model.ai.AIItem
import kotlinx.coroutines.flow.Flow
import java.util.UUID
import kotlin.coroutines.CoroutineContext

interface AIRepository {

    suspend fun add(aiItem: AIItem)
    fun uuidsBy(context: CoroutineContext, deviceSerialNumber: String): Flow<List<String>>
    fun uuids(context: CoroutineContext): Flow<List<String>>
    fun history(context: CoroutineContext, uuid: UUID): Flow<List<AIItem>>
    suspend fun deleteBySerialNumber(deviceSerialNumber: String)
    suspend fun deleteBy(uuid: UUID)
    suspend fun deleteAll()
}