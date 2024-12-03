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
        deviceSerialNumber = aiItem.deviceSerialNumber,
        aiType = aiItem.aiType.name,
        url = aiItem.url,
        model = aiItem.model,
        role = aiItem.role,
        message = aiItem.message,
        dateTime = aiItem.dateTime,
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

    override suspend fun deleteBySerialNumber(deviceSerialNumber: String) {
        aiDao.deleteBy(deviceSerialNumber)
    }

    override suspend fun deleteBy(uuid: UUID) {
        aiDao.deleteBy(uuid.toString())
    }

    override suspend fun deleteAll() = aiDao.nukeTable()

}
