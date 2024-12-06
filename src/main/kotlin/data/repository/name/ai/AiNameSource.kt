package data.repository.name.ai

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.jskako.AiNameQueries
import data.model.items.AiNameItem
import data.model.mappers.toNameItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.UUID
import kotlin.coroutines.CoroutineContext

class AiNameSource(
    private val nameDao: AiNameQueries,
) : AiNameRepository {

    override suspend fun add(aiNameItem: AiNameItem) {
        nameDao.insert(
            sessionUuid = aiNameItem.sessionUuid.toString(),
            name = aiNameItem.name,
            dateTime = aiNameItem.dateTime,
            deviceSerialNumber = aiNameItem.deviceSerialNumber,
            aiType = aiNameItem.aiType.name
        )
    }

    override fun by(sessionUuid: UUID) =
        nameDao.getNameBy(sessionUuid = sessionUuid.toString()).executeAsOneOrNull()?.toNameItem()

    override fun by(context: CoroutineContext): Flow<List<AiNameItem>> =
        nameDao.names().asFlow().mapToList(context).map {
            it.map { name ->
                name.toNameItem()
            }
        }

    override fun uuids(deviceSerialNumber: String): List<UUID> {
        return nameDao.getNamesBySerialNumber(deviceSerialNumber).executeAsList().map { it.toNameItem().sessionUuid }
    }

    override fun update(sessionUuid: UUID, name: String) {
        nameDao.updateName(sessionUuid = sessionUuid.toString(), name = name)
    }

    override suspend fun deleteBy(sessionUuid: UUID) {
        nameDao.deleteBy(sessionUuid = sessionUuid.toString())
    }

    override suspend fun deleteBy(deviceSerialNumber: String) {
        nameDao.deleteBySerialNumber(deviceSerialNumber = deviceSerialNumber)
    }

    override suspend fun deleteAll() = nameDao.nukeTable()
}