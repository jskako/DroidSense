package ui.composable.elements.ai

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import data.model.ai.AIItem
import data.model.ai.ollama.AiRole
import ui.composable.elements.BasicText
import ui.composable.elements.iconButtons.TooltipIconButton
import ui.composable.elements.window.EditDialog
import utils.Colors.darkBlue
import utils.Colors.lightGray
import utils.capitalizeFirstChar
import utils.getStringResource

@Composable
fun ChatCard(
    aiItem: AIItem,
    onUpdate: (String) -> Unit,
    buttonsEnabled: Boolean = true
) {

    var showEditDialog by remember { mutableStateOf(false) }

    if (showEditDialog) {
        EditDialog(
            text = aiItem.message,
            singleLine = false,
            onConfirmRequest = {
                showEditDialog = false
                onUpdate(it)
            },
            onDismissRequest = {
                showEditDialog = false
            }
        )
    }

    Card(
        modifier = Modifier
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
            Row {
                BasicText(
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .weight(1f),
                    value = aiItem.message.capitalizeFirstChar(),
                    fontSize = 16.sp
                )

                if(aiItem.role == AiRole.USER) {
                    TooltipIconButton(
                        isEnabled = buttonsEnabled,
                        tint = if (buttonsEnabled) darkBlue else lightGray,
                        icon = Icons.Default.Edit,
                        tooltip = getStringResource("info.edit.name"),
                        function = {
                            showEditDialog = true
                        }
                    )
                }
            }

            addSpaceHeight(8.dp)

            Text(
                text = "${aiItem.dateTime} - ${aiItem.model}",
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