package adb.application

import com.jskako.droidsense.generated.resources.Res
import com.jskako.droidsense.generated.resources.title_battery_info
import com.jskako.droidsense.generated.resources.title_memory_info
import com.jskako.droidsense.generated.resources.title_network_stats
import com.jskako.droidsense.generated.resources.title_package_info
import org.jetbrains.compose.resources.StringResource

enum class AppDetailType(val title: StringResource) {
    PACKAGE_INFO(Res.string.title_package_info),
    MEMORY_INFO(Res.string.title_memory_info),
    BATTERY_USAGE(Res.string.title_battery_info),
    NETWORK_STATS(Res.string.title_network_stats)
}