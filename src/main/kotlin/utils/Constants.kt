package utils

const val DEFAULT_TIMESTAMP: String = "HH:mm:ss"
const val EXPORT_DATA_TIMESTAMP: String = "yyMMdd-HHmm-ss"
const val LOG_EXTENSION: String = ".log"
const val EMPTY_STRING: String = ""
const val SYSTEM_OS_PROPERTY: String = "os.name"
const val SYSTEM_OS_ARCH: String = "os.arch"
const val ADB_POLLING_INTERVAL_MS = 1000L
const val DEFAULT_DELAY = 1000L
const val ADB_PACKAGE = "adb"
const val SCRCPY_PACKAGE = "scrcpy"
const val ADB_WINDOWS_64_PATH = "tools/scrcpy/windows/64/adb.exe"
const val SCRCPY_WINDOWS_64_PATH = "tools/scrcpy/windows/64/scrcpy.exe"
const val ADB_WINDOWS_32_PATH = "tools/scrcpy/windows/32/adb.exe"
const val SCRCPY_WINDOWS_32_PATH = "tools/scrcpy/windows/32/scrcpy.exe"
const val MIN_WINDOW_WIDTH = 800
const val MIN_WINDOW_HEIGHT = 600
const val ADB_DEVICE_OFFLINE = "offline"

private const val GET_PROPERTY: String = "getprop"
const val DEVICE_MODEL_PROPERTY: String = "$GET_PROPERTY ro.product.model"
const val DEVICE_SERIAL_NUMBER: String = "$GET_PROPERTY ro.serialno"
const val DEVICE_MANUFACTURER: String = "$GET_PROPERTY ro.product.manufacturer"
const val DEVICE_BRAND: String = "$GET_PROPERTY ro.product.brand"
const val DEVICE_BUILD_SDK: String = "$GET_PROPERTY ro.build.version.sdk"
const val DEVICE_ANDROID_VERSION: String = "$GET_PROPERTY ro.build.version.release"
const val DEVICE_DISPLAY_RESOLUTION: String = "wm size"
const val DEVICE_DISPLAY_DENSITY: String = "wm density"
const val DEVICE_IP_ADDRESS: String = "ip route | awk '{print $9}'"
const val DEVICE_PACKAGES: String = "pm list packages"

const val IMAGES_DIRECTORY: String = "images"
const val DEFAULT_PHONE_IMAGE: String = "phone.png"

const val LOG_MANAGER_NUMBER_OF_LINES = 12389
const val LOG_TYPE_REGEX = "DWIEV"
const val APK_EXTENSION = "apk"

const val ABOUT_LIBRARIES_JSON_NAME = "aboutlibraries.json"