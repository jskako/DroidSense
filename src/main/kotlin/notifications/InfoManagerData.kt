package notifications

import androidx.compose.ui.graphics.Color
import data.ArgsText
import utils.Colors.darkBlue

data class InfoManagerData(
    val message: ArgsText,
    val color: Color = darkBlue,
    val duration: Long? = 2000L,
    val buttonVisible: Boolean = false
)
