package ui.application

import androidx.compose.runtime.Composable

data class WindowExtra(
    val screen: @Composable (() -> Unit)? = null,
    val onClose: (() -> Unit)? = null
)
