package utils

enum class OS {
    WINDOWS {
        override fun osName() = "windows"
        override fun path() = "APPDATA"
    },

    MAC {
        override fun osName() = "mac"
        override fun path() = "Library/Application Support"
    },

    LINUX {
        override fun osName() = "linux"
        override fun path() = "~/."
    },

    UNSUPPORTED {
        override fun osName() = "unsupported"
        override fun path() = EMPTY_STRING
    };

    abstract fun osName(): String
    abstract fun path(): String
}