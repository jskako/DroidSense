package requirements

import androidx.compose.ui.graphics.vector.ImageVector

data class RequirementsDetails(
    val description: String,
    val icon: ImageVector,
    val function: () -> Result<Boolean>,
    val descriptionError: String? = null
)