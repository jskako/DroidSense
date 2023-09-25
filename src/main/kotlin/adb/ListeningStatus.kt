package adb

import utils.getStringResource

enum class ListeningStatus {
    LISTENING {
        override fun status() = getStringResource("info.status.listening")
    },
    NOT_LISTENING {
        override fun status(): String = getStringResource("info.status.not.listening")
    };

    abstract fun status(): String
}