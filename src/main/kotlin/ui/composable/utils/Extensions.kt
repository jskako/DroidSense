package ui.composable.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.window.FrameWindowScope
import androidx.compose.ui.window.MenuBar
import ui.application.WindowState
import utils.getStringResource

@Composable
fun FrameWindowScope.createMenu(state: WindowState) {
    MenuBar {
        Menu(getStringResource("info.window.menu")) {
            Item(getStringResource("info.window.exit"), onClick = state.exit)
        }
    }
}