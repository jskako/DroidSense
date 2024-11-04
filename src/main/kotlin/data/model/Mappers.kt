package data.model

import adb.log.LogLevel
import com.jskako.GetUUIDs
import com.jskako.LogHistory
import java.util.UUID

fun GetUUIDs.toUuidItem(): UuidItem {
    return UuidItem(
        uuid = UUID.fromString(this.uuid),
        time = this.time ?: ""
    )
}

fun LogHistory.toLogItem(): LogItem {
    return LogItem(
        uuid = UUID.fromString(this.uuid),
        date = this.date,
        time = this.time,
        pid = this.pid,
        tid = this.tid,
        tag = this.tag,
        level = LogLevel.valueOf(this.level),
        text = this.text
    )
}