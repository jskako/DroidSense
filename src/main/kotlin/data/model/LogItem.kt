package data.model

import adb.log.LogLevel
import java.util.UUID

data class LogItem(
    val uuid: UUID,
    val date: String,
    val time: String,
    val tag: String,
    val packageName: String,
    val type: LogLevel,
    val message: String,
)
