package log

data class LogData(
    val log: String,
    val level: LogLevel
)

enum class LogLevel {
    INFO {
        override fun simplified(): Char = 'I'
    },
    VERBOSE {
        override fun simplified(): Char = 'V'
    },
    DEBUG {
        override fun simplified(): Char = 'D'
    },
    WARNING {
        override fun simplified(): Char = 'W'
    },
    ERROR {
        override fun simplified(): Char = 'E'
    },
    NONE {
        override fun simplified(): Char = 'N'
    };

    abstract fun simplified(): Char
}