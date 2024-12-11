package ui.composable.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.window.FrameWindowScope
import androidx.compose.ui.window.MenuBar
import com.jskako.droidsense.generated.resources.Res
import com.jskako.droidsense.generated.resources.info_window_exit
import com.jskako.droidsense.generated.resources.info_window_menu
import org.jetbrains.compose.resources.stringResource
import ui.application.WindowState

@Composable
fun FrameWindowScope.createMenu(state: WindowState) {
    MenuBar {
        Menu(stringResource(Res.string.info_window_menu)) {
            Item(stringResource(Res.string.info_window_exit), onClick = state.exit)
        }
    }
}