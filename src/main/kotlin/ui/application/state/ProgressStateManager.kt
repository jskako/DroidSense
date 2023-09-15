package ui.application.state

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf

object ProgressStateManager {

    private var _progressVisible = mutableStateOf(false)
    val progressVisible: State<Boolean>
        get() = _progressVisible

    fun isProgressVisible(state: Boolean) {
        _progressVisible.value = state
    }

}
