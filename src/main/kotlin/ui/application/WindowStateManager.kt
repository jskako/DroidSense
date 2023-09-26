package ui.application

object WindowStateManager {

    var windowState: WindowState? = null
        private set

    fun setWindowState(state: WindowState) {
        windowState = state
    }
}