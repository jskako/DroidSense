package data.repository.log

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOneOrDefault
import com.jskako.LogHistoryQueries
import data.model.LogItem
import data.model.UuidItem
import data.model.toLogItem
import data.model.toUuidItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.UUID
import kotlin.coroutines.CoroutineContext

class LogHistorySource(
    private val logDao: LogHistoryQueries,
) : LogRepository {

    override suspend fun add(logItem: LogItem) = logDao.insert(
        uuid = logItem.uuid.toString(),
        name = logItem.name,
        date = logItem.date,
        time = logItem.time,
        tag = logItem.tag,
        packageName = logItem.packageName,
        type = logItem.type.toString(),
        message = logItem.message,
        hasBeenRead = if (logItem.hasBeenRead) 1 else 0
    )

    override fun uuids(context: CoroutineContext): Flow<List<UuidItem>> =
        logDao.getUUIDs().asFlow().mapToList(context).map {
            it.map { getUUIDs ->
                getUUIDs.toUuidItem()
            }
        }

    override fun by(context: CoroutineContext, uuid: UUID): Flow<List<LogItem>> =
        logDao.getDataBy(uuid.toString()).asFlow().mapToList(context).map {
            it.map { logHistory ->
                logHistory.toLogItem()
            }
        }

    override fun countUnreadLogs(context: CoroutineContext, uuid: UUID): Flow<Long> =
        logDao.countUnreadLogsByUUID(uuid.toString()).asFlow().mapToOneOrDefault(
            defaultValue = 0,
            context = context
        )

    override fun countUnreadLogs(context: CoroutineContext) = logDao.countUnreadLogs().asFlow().mapToOneOrDefault(
        defaultValue = 0,
        context = context
    )

    override suspend fun deleteBy(uuid: UUID) {
        logDao.deleteBy(uuid.toString())
    }

    override suspend fun deleteAll() = logDao.nukeTable()
    override suspend fun hasBeenRead(uuid: UUID, hasBeenRead: Boolean) {
        logDao.updateReadState(uuid = uuid.toString(), hasBeenRead = if (hasBeenRead) 1 else 0)
    }

    override suspend fun sessionName(uuid: UUID, name: String) {
        logDao.updateName(uuid = uuid.toString(), name = name)
    }
}
