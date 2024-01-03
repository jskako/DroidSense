package notifications

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import notifications.interfaces.InfoManagerInterface
import utils.EMPTY_STRING

class InfoManager : InfoManagerInterface {

    private var _InfoManagerData = mutableStateOf(InfoManagerData(message = EMPTY_STRING))
    private var job: Job? = null

    val infoManagerData: State<InfoManagerData>
        get() = _InfoManagerData

    override fun showMessage(
        infoManagerData: InfoManagerData,
        scope: CoroutineScope
    ) {
        _InfoManagerData.value = InfoManagerData(
            message = infoManagerData.message,
            color = infoManagerData.color
        )
        if (infoManagerData.duration != null) {
            showTimeLimitedInfoMessage(
                duration = infoManagerData.duration,
                scope = scope
            )
        }
    }

    private fun showTimeLimitedInfoMessage(
        duration: Long,
        scope: CoroutineScope
    ) {
        job?.cancel()
        job = scope.launch {
            delay(duration)
            _InfoManagerData.value = InfoManagerData(message = EMPTY_STRING)
        }
    }

    override fun clearInfoMessage() {
        _InfoManagerData.value = InfoManagerData(message = EMPTY_STRING)
    }
}