package data.repository.log

import data.model.items.LogItem
import kotlinx.coroutines.flow.Flow
import java.util.UUID
import kotlin.coroutines.CoroutineContext

interface LogRepository {

    suspend fun add(logItem: LogItem)
    fun by(context: CoroutineContext, sessionUuid: UUID): Flow<List<LogItem>>
    fun uuids(serialNumber: String): List<UUID>
    suspend fun deleteBy(sessionUuid: UUID)
    suspend fun deleteAll()
}