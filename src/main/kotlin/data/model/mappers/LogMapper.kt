package data.model.mappers

import adb.log.LogLevel
import com.jskako.LogHistory
import data.model.items.LogItem
import java.util.UUID

fun LogHistory.toLogItem(): LogItem {
    return LogItem(
        sessionUuid = UUID.fromString(this.sessionUuid),
        itemUuid = UUID.fromString(this.itemUuid),
        deviceSerialNumber = this.deviceSerialNumber,
        date = this.date,
        time = this.time,
        pid = this.pid,
        tid = this.tid,
        tag = this.tag,
        level = LogLevel.valueOf(this.level),
        text = this.text
    )
}