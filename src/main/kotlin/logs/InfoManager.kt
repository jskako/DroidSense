package logs

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import di.AppModule.provideCoroutineScope
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import logs.interfaces.InfoManagerInterface

object InfoManager : InfoManagerInterface {

    private var _extendedInfo = mutableStateOf(ExtendedInfo())
    private var scope = provideCoroutineScope()
    private var job: Job? = null

    val extendedInfo: State<ExtendedInfo>
        get() = _extendedInfo

    override fun showInfoMessage(
        message: String,
        backgroundColor: Color,
        duration: Long
    ) {
        _extendedInfo.value = ExtendedInfo(message, backgroundColor)
        job?.cancel()
        job = scope.launch(Default) {
            delay(duration)
            _extendedInfo.value = ExtendedInfo()
        }
    }
}