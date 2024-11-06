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
        nameDao.insert(uuid = nameItem.uuid.toString(), name = nameItem.name)
    }

    override fun by(uuid: UUID) = nameDao.getNameBy(uuid = uuid.toString()).executeAsOneOrNull()?.toNameItem()

    override fun by(context: CoroutineContext): Flow<List<NameItem>> =
        nameDao.names().asFlow().mapToList(context).map {
            it.map { name ->
                name.toNameItem()
            }
        }

    override fun update(uuid: UUID, name: String) {
        nameDao.updateName(uuid = uuid.toString(), name = name)
    }

    override suspend fun deleteBy(uuid: UUID) {
        nameDao.deleteBy(uuid = uuid.toString())
    }

    override suspend fun deleteAll() = nameDao.nukeTable()

}