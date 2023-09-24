package notifications

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import di.AppModule.provideCoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import notifications.interfaces.InfoManagerInterface

object InfoManager : InfoManagerInterface {

    private var _extendedInfo = mutableStateOf(ExtendedInfo())
    private var scope = provideCoroutineScope()
    private var job: Job? = null

    val extendedInfo: State<ExtendedInfo>
        get() = _extendedInfo

    override fun showTimeLimitedInfoMessage(
        message: String,
        backgroundColor: Color,
        duration: Long
    ) {
        _extendedInfo.value = ExtendedInfo(message, backgroundColor)
        job?.cancel()
        job = scope.launch {
            delay(duration)
            _extendedInfo.value = ExtendedInfo()
        }
    }

    override fun showInfoMessage(
        message: String,
        backgroundColor: Color
    ) {
        _extendedInfo.value = ExtendedInfo(message, backgroundColor)
    }

    override fun clearInfoMessage() {
        _extendedInfo.value = ExtendedInfo()
    }
}