package data.repository.log

import data.model.LogItem
import data.model.UuidItem
import kotlinx.coroutines.flow.Flow
import java.util.UUID

interface LogRepository {

    suspend fun add(logItem: LogItem)
    fun by(uuid: UUID): Flow<List<LogItem>>
    fun uuids(): Flow<List<UuidItem>>
    fun countUnreadLogs(uuid: UUID): Flow<Long>
    fun countUnreadLogs(): Flow<Long>
    suspend fun deleteBy(uuid: UUID)
    suspend fun deleteAll()
    suspend fun hasBeenRead(uuid: UUID, hasBeenRead: Boolean)
    suspend fun sessionName(uuid: UUID, name: String)
}