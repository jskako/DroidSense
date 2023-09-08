package utils

enum class OS {
    WINDOWS {
        override fun name() = "windows"
        override fun path() = "APPDATA"
    },

    MAC {
        override fun name() = "mac"
        override fun path() = "Library/Application Support"
    },

    LINUX {
        override fun name() = "linux"
        override fun path() = "~/."
    },

    UNSUPPORTED {
        override fun name() = "unsupported"
        override fun path() = EMPTY_STRING
    };

    abstract fun name(): String
    abstract fun path(): String
}