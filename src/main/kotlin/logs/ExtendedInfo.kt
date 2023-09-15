package logs

import androidx.compose.ui.graphics.Color

data class ExtendedInfo(
    val message: String = "",
    val color: Color = Color.Gray,
    val functions: Map<String, () -> Unit>? = null,
)
