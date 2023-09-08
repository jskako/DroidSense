package logs

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import di.AppModule.provideCoroutineScope
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import logs.interfaces.InfoManagerInterface
import utils.EMPTY_STRING

object InfoManager : InfoManagerInterface {

    private var _message = mutableStateOf(EMPTY_STRING)
    private var scope = provideCoroutineScope()
    private var job: Job? = null

    val message: State<String>
        get() = _message

    override fun showInfoMessage(message: String, duration: Long) {
        _message.value = message
        job?.cancel()
        job = scope.launch(Default) {
            delay(duration)
            _message.value = EMPTY_STRING
        }
    }
}