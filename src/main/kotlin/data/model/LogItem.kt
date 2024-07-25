package data.model

import java.util.UUID
import log.LogLevel

data class LogItem(
    val uuid: UUID,
    val name: String,
    val date: String,
    val time: String,
    val tag: String,
    val packageName: String,
    val type: LogLevel,
    val message: String,
    val hasBeenRead: Boolean
)
