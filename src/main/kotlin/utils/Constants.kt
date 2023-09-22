package utils

const val APP_NAME: String = "DroidSense"
const val DEFAULT_TIMESTAMP: String = "HH:mm:ss"
const val EXPORT_DATA_TIMESTAMP: String = "yyMMdd-HHmm-ss"
const val DEFAULT_WEB: String = "https://www.google.com/"
const val LOG_EXTENSION: String = ".log"
const val EMPTY_STRING: String = ""
const val SYSTEM_OS_PROPERTY: String = "os.name"
const val SYSTEM_HOME_PROPERTY: String = "user.home"
const val ADB_POLLING_INTERVAL_MS = 1000L

private const val GET_PROPERTY: String = "getprop"
const val DEVICE_MODEL_PROPERTY: String = "$GET_PROPERTY ro.product.model"
const val DEVICE_MANUFACTURER: String = "$GET_PROPERTY ro.product.manufacturer"
const val DEVICE_BRAND: String = "$GET_PROPERTY ro.product.brand"
const val DEVICE_BUILD_SDK: String = "$GET_PROPERTY ro.build.version.sdk"
const val DEVICE_ANDROID_VERSION: String = "$GET_PROPERTY ro.build.version.release"
const val DEVICE_DISPLAY_RESOLUTION: String = "wm size"
const val DEVICE_DISPLAY_DENSITY: String = "wm density"
const val DEVICE_IP_ADDRESS: String = "ip route | awk '{print $9}'"