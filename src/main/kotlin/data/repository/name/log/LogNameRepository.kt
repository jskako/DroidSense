package data.repository.name.log

import data.model.items.LogNameItem
import kotlinx.coroutines.flow.Flow
import java.util.UUID
import kotlin.coroutines.CoroutineContext

interface LogNameRepository {

    suspend fun add(logNameItem: LogNameItem)
    fun by(context: CoroutineContext): Flow<List<LogNameItem>>
    fun by(sessionUuid: UUID): LogNameItem?
    fun uuids(serialNumber: String): List<UUID>
    fun update(sessionUuid: UUID, name: String)
    suspend fun deleteBy(sessionUuid: UUID)
    suspend fun deleteAll()
}