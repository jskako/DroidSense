package adb

import com.jskako.droidsense.generated.resources.Res
import com.jskako.droidsense.generated.resources.info_status_monitoring
import com.jskako.droidsense.generated.resources.info_status_not_monitoring
import org.jetbrains.compose.resources.StringResource

enum class MonitoringStatus {
    MONITORING {
        override fun status() = Res.string.info_status_monitoring
    },

    NOT_MONITORING {
        override fun status() = Res.string.info_status_not_monitoring
    };

    abstract fun status(): StringResource
}