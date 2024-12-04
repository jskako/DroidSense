package data.repository.ai

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.jskako.AIHistoryQueries
import data.model.ai.AIItem
import data.model.mappers.toAiItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.UUID
import kotlin.coroutines.CoroutineContext

class AIHistorySource(
    private val aiDao: AIHistoryQueries,
) : AIRepository {

    override suspend fun add(aiItem: AIItem) = aiDao.insert(
        uuid = aiItem.uuid.toString(),
        messageUUID = aiItem.messageUUID.toString(),
        deviceSerialNumber = aiItem.deviceSerialNumber,
        aiType = aiItem.aiType.name,
        url = aiItem.url,
        model = aiItem.model,
        role = aiItem.role.databaseName(),
        message = aiItem.message,
        dateTime = aiItem.dateTime,
        succeed = if(aiItem.succeed) 1L else 0L
    )

    override fun uuidsBy(context: CoroutineContext, deviceSerialNumber: String): Flow<List<String>> =
        aiDao.getUUIDsBySerialNumber(deviceSerialNumber).asFlow().mapToList(context)

    override fun uuids(context: CoroutineContext): Flow<List<String>> =
        aiDao.getUUIDs().asFlow().mapToList(context)

    override fun history(context: CoroutineContext, uuid: UUID): Flow<List<AIItem>> {
        return aiDao.history(uuid.toString()).asFlow().mapToList(context).map {
            it.map { aiHistory ->
                aiHistory.toAiItem()
            }
        }
    }

    override suspend fun updateSucceed(messageUUID: UUID, succeed: Boolean) {
        aiDao.updateSucceed(messageUUID = messageUUID.toString(), succeed = if (succeed) 1L else 0L)
    }

    override suspend fun updateMessage(messageUUID: UUID, message: String) {
        aiDao.updateMessage(messageUUID = messageUUID.toString(), message = message)
    }

    override suspend fun deleteBySerialNumber(deviceSerialNumber: String) {
        aiDao.deleteBy(deviceSerialNumber)
    }

    override suspend fun deleteBy(uuid: UUID) {
        aiDao.deleteBy(uuid.toString())
    }

    override suspend fun deleteMessagesAbove(messageUUID: UUID) {
        aiDao.deleteMessagesAbove(messageUUID.toString())
    }

    override suspend fun deleteAll() = aiDao.nukeTable()

}
