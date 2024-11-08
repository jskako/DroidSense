package data.repository.log

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.jskako.LogHistoryQueries
import data.model.items.LogItem
import data.model.mappers.toLogItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.UUID
import kotlin.coroutines.CoroutineContext

class LogHistorySource(
    private val logDao: LogHistoryQueries,
) : LogRepository {

    override suspend fun add(logItem: LogItem) = logDao.insert(
        sessionUuid = logItem.sessionUuid.toString(),
        itemUuid = logItem.itemUuid.toString(),
        phoneSerialNumber = logItem.phoneSerialNumber,
        date = logItem.date,
        time = logItem.time,
        pid = logItem.pid,
        tid = logItem.tid,
        tag = logItem.tag,
        level = logItem.level.toString(),
        text = logItem.text
    )

    override fun by(context: CoroutineContext, sessionUuid: UUID): Flow<List<LogItem>> =
        logDao.getDataBy(sessionUuid.toString()).asFlow().mapToList(context).map {
            it.map { logHistory ->
                logHistory.toLogItem()
            }
        }

    override fun uuids(serialNumber: String): List<UUID> {
        return logDao.getUUIDsBySerialNumber(serialNumber).executeAsList().map { UUID.fromString(it) }
    }

    override suspend fun deleteBy(sessionUuid: UUID) {
        logDao.deleteBy(sessionUuid.toString())
    }

    override suspend fun deleteAll() = logDao.nukeTable()

}
