package log

import java.util.UUID

data class LogData(
    val uuid: UUID = UUID.randomUUID(),
    val time: String,
    val log: String,
    val level: LogLevel,
    val isSelected: Boolean = false
) {
    override fun toString(): String {
        return buildString {
            appendLine("$level - $time")
            appendLine(log)
        }
    }
}