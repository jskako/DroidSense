package ui.application

import androidx.compose.runtime.Composable

sealed class ApplicationEvent {
    data class NewWindow(
        val startingComposable: @Composable () -> Unit
    ) : ApplicationEvent()
}