package adb.application

enum class AppDetailType(val title: String) {
    PACKAGE_INFO("Package Info"),
    MEMORY_INFO("Memory Info"),
    BATTERY_USAGE("Battery Usage"),
    NETWORK_STATS("Network Stats")
}