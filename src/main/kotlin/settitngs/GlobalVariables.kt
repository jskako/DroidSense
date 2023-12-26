package settitngs

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import utils.ADB_PACKAGE
import utils.SCRCPY_PACKAGE

class GlobalVariables {

    private val _adbPath = mutableStateOf(ADB_PACKAGE)
    private val _scrCpyPath = mutableStateOf(SCRCPY_PACKAGE)

    val adbPath: State<String>
        get() = _adbPath

    val scrCpyPath: State<String>
        get() = _scrCpyPath

    fun setAdbPath(adbPath: String) {
        _adbPath.value = adbPath
    }

    fun setScrCpyPath(scrCpy: String) {
        _scrCpyPath.value = scrCpy
    }
}