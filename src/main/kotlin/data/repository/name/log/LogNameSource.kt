package data.repository.name.log

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.jskako.LogNameQueries
import data.model.items.LogNameItem
import data.model.mappers.toNameItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.UUID
import kotlin.coroutines.CoroutineContext

class LogNameSource(
    private val nameDao: LogNameQueries,
) : LogNameRepository {

    override suspend fun add(logNameItem: LogNameItem) {
        nameDao.insert(
            sessionUuid = logNameItem.sessionUuid.toString(),
            name = logNameItem.name,
            dateTime = logNameItem.dateTime,
            deviceSerialNumber = logNameItem.deviceSerialNumber,
        )
    }

    override fun by(sessionUuid: UUID) =
        nameDao.getNameBy(sessionUuid = sessionUuid.toString()).executeAsOneOrNull()?.toNameItem()

    override fun by(context: CoroutineContext): Flow<List<LogNameItem>> =
        nameDao.names().asFlow().mapToList(context).map {
            it.map { name ->
                name.toNameItem()
            }
        }

    override fun uuids(serialNumber: String): List<UUID> {
        return nameDao.getNamesBySerialNumber(serialNumber).executeAsList().map { it.toNameItem().sessionUuid }
    }

    override fun update(sessionUuid: UUID, name: String) {
        nameDao.updateName(sessionUuid = sessionUuid.toString(), name = name)
    }

    override suspend fun deleteBy(sessionUuid: UUID) {
        nameDao.deleteBy(sessionUuid = sessionUuid.toString())
    }

    override suspend fun deleteAll() = nameDao.nukeTable()

}