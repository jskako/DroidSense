package data.repository.log

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
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
        date = logItem.date,
        time = logItem.time,
        pid = logItem.pid,
        tid = logItem.tid,
        tag = logItem.tag,
        level = logItem.level.toString(),
        text = logItem.text
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

    override suspend fun deleteBy(uuid: UUID) {
        logDao.deleteBy(uuid.toString())
    }

    override suspend fun deleteAll() = logDao.nukeTable()

}
