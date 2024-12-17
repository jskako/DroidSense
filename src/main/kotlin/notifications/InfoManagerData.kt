package notifications

import androidx.compose.ui.graphics.Color
import data.ArgsText
import utils.Colors.darkBlue

data class InfoManagerData(
    val message: ArgsText,
    val color: Color = darkBlue,
    val duration: Long? = 4000L,
    val buttonVisible: Boolean = false
)
