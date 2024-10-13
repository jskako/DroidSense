package ui.composable.elements.iconButtons

import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import utils.Colors


data class IconButtonsData(
    val modifier: Modifier = Modifier,
    val icon: ImageVector,
    val contentDescription: String,
    val tint: Color = Colors.darkBlue,
    val function: () -> Unit,
    val isEnabled: Boolean = true
)