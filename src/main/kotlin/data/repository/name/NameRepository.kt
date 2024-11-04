package data.repository.name

import data.model.items.NameItem
import java.util.UUID

interface NameRepository {

    suspend fun add(nameItem: NameItem)
    fun by(uuid: UUID): NameItem?
    fun update(uuid: UUID, name: String)
    suspend fun deleteBy(uuid: UUID)
    suspend fun deleteAll()
}