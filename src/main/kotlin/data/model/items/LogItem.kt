package data.model.items

import adb.log.LogLevel
import java.util.UUID

data class LogItem(
    val uuid: UUID,
    val phoneSerialNumber: String,
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
            appendLine("$level - $time")
            appendLine(text)
        }
    }
}
