package notifications

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.graphics.Color
import notifications.InfoManager.showInfoMessage
import notifications.interfaces.LogManagerInterface
import utils.getStringResource
import utils.getTimeStamp

object LogManager : LogManagerInterface {

    private val _logs = mutableStateListOf<LogDetails>()

    val logs: List<LogDetails> get() = _logs

    override fun addLog(
        log: String, documentPath: String, extra: ExtendedLog?
    ) {
        _logs.add(LogDetails(getTimeStamp(), log, documentPath, extra))
        println(log)
    }

    override fun clearLogs() {
        _logs.clear()
        showInfoMessage(
            message = getStringResource("info.log.clear"),
            backgroundColor = Color.Red
        )
    }

}