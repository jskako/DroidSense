package notifications

import utils.EMPTY_STRING

data class LogDetails(
    val time: String? = EMPTY_STRING,
    val log: String? = EMPTY_STRING,
    val documentPath: String = EMPTY_STRING,
    val extra: ExtendedLog? = null
)