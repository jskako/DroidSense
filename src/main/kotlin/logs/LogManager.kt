package logs

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import logs.interfaces.LogManagerInterface
import utils.getStringResource
import utils.getTimeStamp

object LogManager : LogManagerInterface {

    private val _logs = mutableStateOf(
        listOf(LogDetails(getTimeStamp(), getStringResource("info.log.general")))
    )

    val logs: State<List<LogDetails>>
        get() = _logs

    override fun addLog(
        log: String, documentPath: String, extra: ExtendedLog?
    ) {
        _logs.value += (listOf(LogDetails(getTimeStamp(), log, documentPath, extra)))
        println(log)
    }

    override fun clearLogs() {
        _logs.value = listOf(LogDetails(getTimeStamp(), getStringResource("info.log.clear")))
    }

}