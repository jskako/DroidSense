package ui.application

sealed class ApplicationEvent {
    data object NavWindow : ApplicationEvent()
}