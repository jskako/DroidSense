package ui.composable.sections.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FolderOpen
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import data.DATABASE_NAME
import notifications.InfoManagerData
import ui.composable.elements.OutlinedText
import ui.composable.elements.iconButtons.TooltipIconButton
import utils.Colors.darkBlue
import utils.getStringResource
import utils.openFolderAtPath
import kotlin.io.path.Path
import kotlin.io.path.absolutePathString

@Composable
fun DatabaseSection(
    onMessage: (InfoManagerData) -> Unit
) {

    val databasePath by remember {
        mutableStateOf(
            Path(System.getProperty("user.home"), "DroidSense").resolve(DATABASE_NAME).absolutePathString()
        )
    }

    Column(
        modifier = Modifier
            .padding(vertical = 16.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedText(
                modifier = Modifier.weight(1f),
                readOnly = true,
                text = databasePath,
                hintText = getStringResource("info.database.location"),
                onValueChanged = {}
            )

            TooltipIconButton(
                modifier = Modifier.padding(end = 16.dp),
                tint = darkBlue,
                icon = Icons.Default.FolderOpen,
                tooltip = getStringResource("info.show.folder"),
                function = {
                    openFolderAtPath(databasePath.substringBeforeLast("/"))
                }
            )
        }
    }
}