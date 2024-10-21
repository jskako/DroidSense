package notifications

import androidx.compose.ui.graphics.Color
import utils.Colors.darkBlue

data class InfoManagerData(
    val message: String,
    val color: Color = darkBlue,
    val duration: Long? = 2000L,
    val buttonVisible: Boolean = false
)
