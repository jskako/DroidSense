package ui.composable.elements.history

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ui.composable.elements.BasicText
import ui.composable.elements.BasicTextCaption
import ui.composable.elements.iconButtons.TooltipIconButton
import ui.composable.elements.window.EditDialog
import ui.composable.elements.window.TextDialog
import utils.Colors.darkBlue
import utils.Colors.lightGray
import utils.capitalizeFirstChar
import utils.getStringResource
import java.util.UUID

@Composable
fun NameCard(
    uuid: UUID,
    name: String,
    dateTime: String,
    onUpdate: (String) -> Unit,
    onClick: () -> Unit,
    onDelete: () -> Unit,
    buttonsEnabled: Boolean = true
) {

    var showEditDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    if (showEditDialog) {
        EditDialog(
            text = name,
            onConfirmRequest = {
                showEditDialog = false
                onUpdate(it)
            },
            onDismissRequest = {
                showEditDialog = false
            }
        )
    }

    if (showDeleteDialog) {
        TextDialog(
            title = getStringResource("info.delete.log.title"),
            description = buildString {
                appendLine(getStringResource("info.delete.log.description"))
                appendLine(uuid)
            },
            onConfirmRequest = {
                showDeleteDialog = false
                onDelete()
            },
            onDismissRequest = {
                showDeleteDialog = false
            }
        )
    }

    Card(
        modifier = Modifier
            .clickable {
                onClick()
            }
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                BasicText(
                    value = name.capitalizeFirstChar(),
                    fontSize = 16.sp,
                    isBold = true,
                )

                Spacer(modifier = Modifier.weight(1f))

                TooltipIconButton(
                    isEnabled = buttonsEnabled,
                    tint = if (buttonsEnabled) darkBlue else lightGray,
                    icon = Icons.Default.Edit,
                    tooltip = getStringResource("info.edit.name"),
                    function = {
                        showEditDialog = true
                    }
                )

                addSpaceHeight(4.dp)

                TooltipIconButton(
                    isEnabled = buttonsEnabled,
                    tint = if (buttonsEnabled) darkBlue else lightGray,
                    icon = Icons.Default.Delete,
                    tooltip = getStringResource("info.delete"),
                    function = {
                        showDeleteDialog = true
                    }
                )
            }

            addSpaceHeight(4.dp)

            BasicTextCaption(
                text1 = getStringResource("info.uuid.identifier"),
                text2 = uuid.toString()
            )

            addSpaceHeight(8.dp)

            Text(
                text = dateTime,
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
        }
    }
}

@Composable
private fun addSpaceHeight(height: Dp = 5.dp) {
    Spacer(modifier = Modifier.height(height))
}