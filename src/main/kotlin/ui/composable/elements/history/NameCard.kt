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
import data.model.items.NameItem
import data.repository.name.NameSource
import notifications.InfoManagerData
import ui.composable.elements.BasicText
import ui.composable.elements.BasicTextCaption
import ui.composable.elements.iconButtons.TooltipIconButton
import ui.composable.elements.window.EditDialog
import utils.Colors.darkBlue
import utils.Colors.lightGray
import utils.capitalizeFirstChar
import utils.getStringResource

@Composable
fun NameCard(
    nameItem: NameItem,
    nameSource: NameSource,
    onClick: () -> Unit,
    onDelete: () -> Unit,
    deleteInProgress: Boolean,
    onDeleteInProgress: (Boolean) -> Unit,
    onMessage: (InfoManagerData) -> Unit
) {

    var showDialog by remember { mutableStateOf(false) }

    if (showDialog) {
        EditDialog(
            text = nameItem.name,
            onConfirmRequest = {
                showDialog = false
                nameSource.update(
                    sessionUuid = nameItem.sessionUuid,
                    name = it
                )
            },
            onDismissRequest = {
                showDialog = false
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
                    value = nameItem.name.capitalizeFirstChar(),
                    fontSize = 16.sp,
                    isBold = true,
                )

                Spacer(modifier = Modifier.weight(1f))

                TooltipIconButton(
                    isEnabled = !deleteInProgress,
                    tint = if (deleteInProgress) lightGray else darkBlue,
                    icon = Icons.Default.Edit,
                    tooltip = getStringResource("info.edit.name"),
                    function = {
                        showDialog = true
                    }
                )

                addSpaceHeight(4.dp)

                TooltipIconButton(
                    isEnabled = !deleteInProgress,
                    tint = if (deleteInProgress) lightGray else darkBlue,
                    icon = Icons.Default.Delete,
                    tooltip = getStringResource("info.delete"),
                    function = {
                        onDeleteInProgress(true)
                        onDelete()
                    }
                )
            }

            addSpaceHeight(4.dp)

            BasicTextCaption(
                text1 = getStringResource("info.uuid.identifier"),
                text2 = nameItem.sessionUuid.toString()
            )

            addSpaceHeight(8.dp)

            Text(
                text = nameItem.dateTime,
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