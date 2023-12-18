package log

data class LogData(
    val time: String,
    val log: String,
    val level: LogLevel
)