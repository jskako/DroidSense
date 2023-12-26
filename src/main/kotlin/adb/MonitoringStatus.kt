package adb

import utils.getStringResource

enum class MonitoringStatus {
    MONITORING {
        override fun status() = getStringResource("info.status.monitoring")
    },
    NOT_MONITORING {
        override fun status(): String = getStringResource("info.status.not.monitoring")
    };

    abstract fun status(): String
}