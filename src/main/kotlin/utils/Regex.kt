package utils

val logLevelRegex = Regex("\\b[$LOG_TYPE_REGEX]\\b")
val deviceRegex = Regex(".*device\\s+.*")
val ipPortRegex = Regex("""^\d{1,3}\.\d{1,3}\.\d{1,3}\.\d{1,3}:\d+$""")