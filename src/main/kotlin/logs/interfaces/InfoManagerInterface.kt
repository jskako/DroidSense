package logs.interfaces

import androidx.compose.ui.graphics.Color

interface InfoManagerInterface {
    fun showInfoMessage(
        message: String,
        backgroundColor: Color = Color.Gray,
        duration: Long = 2000L)
}