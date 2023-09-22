package utils

enum class OS {
    WINDOWS {
        override fun osName() = "windows"
    },

    MAC {
        override fun osName() = "mac"
    },

    LINUX {
        override fun osName() = "linux"
    },

    UNSUPPORTED {
        override fun osName() = "unsupported"
    };

    abstract fun osName(): String
}