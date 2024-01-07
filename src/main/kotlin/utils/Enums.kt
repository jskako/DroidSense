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

enum class Arch {
    BIT_32 {
        override fun archName() = "32"
    },

    BIT_64 {
        override fun archName() = "64"
    },

    UNSUPPORTED {
        override fun archName() = "unsupported"
    };

    abstract fun archName(): String
}