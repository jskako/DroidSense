package data.repository.log

import data.model.items.LogItem
import data.model.items.UuidItem
import kotlinx.coroutines.flow.Flow
import java.util.UUID
import kotlin.coroutines.CoroutineContext

interface LogRepository {

    suspend fun add(logItem: LogItem)
    fun by(context: CoroutineContext, uuid: UUID): Flow<List<LogItem>>
    fun uuids(context: CoroutineContext): Flow<List<UuidItem>>
    fun uuids(serialNumber: String): List<UUID>
    suspend fun deleteBy(uuid: UUID)
    suspend fun deleteAll()
}