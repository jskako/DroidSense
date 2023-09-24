package notifications.interfaces

import androidx.compose.ui.graphics.Color
import utils.Colors.darkBlue

interface InfoManagerInterface {
    fun showTimeLimitedInfoMessage(
        message: String,
        backgroundColor: Color = darkBlue,
        duration: Long = 2000L
    )

    fun showInfoMessage(
        message: String,
        backgroundColor: Color = darkBlue
    )

    fun clearInfoMessage()
}