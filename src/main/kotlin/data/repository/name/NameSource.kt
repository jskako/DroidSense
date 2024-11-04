package data.repository.name

import com.jskako.CustomNameQueries
import data.model.items.NameItem
import data.model.mappers.toNameItem
import java.util.UUID
import kotlin.uuid.ExperimentalUuidApi

class NameSource(
    private val nameDao: CustomNameQueries,
) : NameRepository {

    override suspend fun add(nameItem: NameItem) {
        nameDao.insert(uuid = nameItem.uuid.toString(), name = nameItem.name)
    }

    override fun by(uuid: UUID) = nameDao.getNameBy(uuid = uuid.toString()).executeAsOneOrNull()?.toNameItem()

    override fun update(uuid: UUID, name: String) {
        nameDao.updateName(uuid = uuid.toString(), name = name)
    }

    @ExperimentalUuidApi
    override suspend fun deleteBy(uuid: UUID) {
        nameDao.deleteBy(uuid = uuid.toString())
    }

    override suspend fun deleteAll() = nameDao.nukeTable()

}