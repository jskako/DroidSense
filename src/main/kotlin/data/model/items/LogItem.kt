package data.model.items

import adb.log.LogLevel
import java.util.UUID

data class LogItem(
    val sessionUuid: UUID,
    val itemUuid: UUID,
    val deviceSerialNumber: String,
    val date: String,
    val time: String,
    val pid: Long,
    val tid: Long,
    val tag: String,
    val level: LogLevel,
    val text: String,
    val isSelected: Boolean = false
) {
    override fun toString(): String {
        return buildString {
            appendLine("$date $time $level $pid-$tid $tag")
            appendLine(text)
        }
    }
}
