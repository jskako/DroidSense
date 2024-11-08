package data.repository.name

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.jskako.CustomNameQueries
import data.model.items.NameItem
import data.model.mappers.toNameItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.UUID
import kotlin.coroutines.CoroutineContext

class NameSource(
    private val nameDao: CustomNameQueries,
) : NameRepository {

    override suspend fun add(nameItem: NameItem) {
        nameDao.insert(
            sessionUuid = nameItem.sessionUuid.toString(),
            name = nameItem.name,
            dateTime = nameItem.dateTime,
            deviceSerialNumber = nameItem.deviceSerialNumber,
        )
    }

    override fun by(sessionUuid: UUID) =
        nameDao.getNameBy(sessionUuid = sessionUuid.toString()).executeAsOneOrNull()?.toNameItem()

    override fun by(context: CoroutineContext): Flow<List<NameItem>> =
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