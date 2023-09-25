package utils

const val DEFAULT_TIMESTAMP: String = "HH:mm:ss"
const val EXPORT_DATA_TIMESTAMP: String = "yyMMdd-HHmm-ss"
const val DEFAULT_WEB: String = "https://www.google.com/"
const val LOG_EXTENSION: String = ".log"
const val EMPTY_STRING: String = ""
const val SYSTEM_OS_PROPERTY: String = "os.name"
const val ADB_POLLING_INTERVAL_MS = 1000L
const val DEFAULT_DELAY = 1000L
const val ADB_PACKAGE = "adb"
const val SCRCPY_PACKAGE = "scrcpy"
const val ADB_WINDOWS_PATH = "tools/scrcpy/windows/adb.exe"
const val SCRCPY_WINDOWS_PATH = "tools/scrcpy/windows/scrcpy.exe"

private const val GET_PROPERTY: String = "getprop"
const val DEVICE_MODEL_PROPERTY: String = "$GET_PROPERTY ro.product.model"
const val DEVICE_MANUFACTURER: String = "$GET_PROPERTY ro.product.manufacturer"
const val DEVICE_BRAND: String = "$GET_PROPERTY ro.product.brand"
const val DEVICE_BUILD_SDK: String = "$GET_PROPERTY ro.build.version.sdk"
const val DEVICE_ANDROID_VERSION: String = "$GET_PROPERTY ro.build.version.release"
const val DEVICE_DISPLAY_RESOLUTION: String = "wm size"
const val DEVICE_DISPLAY_DENSITY: String = "wm density"
const val DEVICE_IP_ADDRESS: String = "ip route | awk '{print $9}'"

const val IMAGES_DIRECTORY: String = "images"
const val DEFAULT_PHONE_IMAGE: String = "phone.png"