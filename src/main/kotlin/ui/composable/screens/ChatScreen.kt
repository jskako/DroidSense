package ui.composable.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import notifications.InfoManager
import ui.composable.sections.ai.ChatSection
import ui.composable.sections.info.InfoSection

@Composable
fun ChatScreen() {

    val infoManager = remember { InfoManager() }
    val scope = rememberCoroutineScope()

    Column {

        InfoSection(
            onCloseClicked = { infoManager.clearInfoMessage() },
            message = infoManager.infoManagerData.value.message,
            color = infoManager.infoManagerData.value.color
        )

        ChatSection(
            onMessage = {
                infoManager.showMessage(
                    infoManagerData = it,
                    scope = scope
                )
            }
        )
    }
}