package ui.application

sealed class ApplicationEvent {
    data object NewWindow : ApplicationEvent()
}