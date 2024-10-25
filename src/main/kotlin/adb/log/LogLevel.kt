package adb.log

import androidx.compose.ui.graphics.Color

enum class LogLevel {
    ERROR {
        override fun simplified(): Char = 'E'
        override fun getLogColor(): Pair<Color, Color> = Color.Red to Color.Black
    },
    WARN {
        override fun simplified(): Char = 'W'
        override fun getLogColor(): Pair<Color, Color> = Color.Yellow to Color.Black
    },
    INFO {
        override fun simplified(): Char = 'I'
        override fun getLogColor(): Pair<Color, Color> = Color.Green to Color.White
    },
    DEBUG {
        override fun simplified(): Char = 'D'
        override fun getLogColor(): Pair<Color, Color> = Color.Blue to Color.White
    },
    VERBOSE {
        override fun simplified(): Char = 'V'
        override fun getLogColor(): Pair<Color, Color> = Color.White to Color.Black
    },
    NONE {
        override fun simplified(): Char = 'N'
        override fun getLogColor(): Pair<Color, Color> = Color.White to Color.Black
    };

    abstract fun simplified(): Char
    abstract fun getLogColor(): Pair<Color, Color>
}