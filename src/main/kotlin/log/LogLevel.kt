package log

enum class LogLevel {
    ERROR {
        override fun simplified(): Char = 'E'
    },
    WARN {
        override fun simplified(): Char = 'W'
    },
    INFO {
        override fun simplified(): Char = 'I'
    },
    DEBUG {
        override fun simplified(): Char = 'D'
    },
    VERBOSE {
        override fun simplified(): Char = 'V'
    },
    NONE {
        override fun simplified(): Char = 'N'
    };

    abstract fun simplified(): Char
}