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
    suspend fun updateSucceed(messageUUID: UUID, succeed: Boolean)
    suspend fun updateMessage(messageUUID: UUID, message: String)
    suspend fun deleteBySerialNumber(deviceSerialNumber: String)
    suspend fun deleteMessagesAbove(messageUUID: UUID)
    suspend fun deleteBy(uuid: UUID)
    suspend fun deleteAll()
}