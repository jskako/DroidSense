package settitngs

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import utils.EMPTY_STRING

class GlobalVariables {

    private val _adbPath = mutableStateOf(EMPTY_STRING)
    private val _scrCpyPath = mutableStateOf(EMPTY_STRING)

    val adbPath: State<String>
        get() = _adbPath

    val scrCpyPath: State<String>
        get() = _scrCpyPath

    val isValid = _adbPath.value.isNotEmpty() && _scrCpyPath.value.isNotEmpty()

    fun setAdbPath(adbPath: String) {
        _adbPath.value = adbPath
    }

    fun setScrCpyPath(scrCpy: String) {
        _scrCpyPath.value = scrCpy
    }
}