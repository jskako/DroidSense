package data.model

import com.jskako.GetUUIDs
import com.jskako.LogHistory
import adb.log.LogLevel
import java.util.UUID

fun GetUUIDs.toUuidItem(): UuidItem {
    return UuidItem(
        uuid = UUID.fromString(this.uuid),
        name = this.name,
        time = this.time ?: "",
        hasBeenRead = this.hasBeenRead == 1L
    )
}

fun LogHistory.toLogItem(): LogItem {
    return LogItem(
        uuid = UUID.fromString(this.uuid),
        name = this.name,
        date = this.date,
        time = this.time,
        tag = this.tag,
        packageName = this.packageName,
        type = LogLevel.valueOf(this.type),
        message = this.message,
        hasBeenRead = this.hasBeenRead == 1L
    )
}