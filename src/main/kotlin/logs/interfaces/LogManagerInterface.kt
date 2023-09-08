package logs.interfaces

import logs.ExtendedLog
import utils.EMPTY_STRING

interface LogManagerInterface {
    fun addLog(
        log: String,
        documentPath: String = EMPTY_STRING,
        extra: ExtendedLog? = null
    )
    fun clearLogs()
}