package ui.composable.elements.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import ui.composable.elements.BasicText
import ui.composable.elements.iconButtons.TooltipIconButton
import ui.composable.elements.window.EditDialog
import ui.composable.elements.window.TextDialog
import utils.Colors.darkBlue
import utils.getStringResource

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DeleteEditRowCard(
    text: String,
    editTitle: String,
    deleteDialogDescription: String,
    onEdit: (String) -> Unit,
    onSelected: (() -> Unit)? = null,
    onDelete: () -> Unit,
    isSelected: Boolean = false
) {

    var showEditDialog by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }

    if (showEditDialog) {
        EditDialog(
            title = editTitle,
            text = text,
            onConfirmRequest = {
                showEditDialog = false
                onEdit(it)
            },
            onDismissRequest = {
                showEditDialog = false
            }
        )
    }

    if (showDialog) {
        TextDialog(
            title = getStringResource("info.delete.log.title"),
            description = deleteDialogDescription,
            onConfirmRequest = {
                showDialog = false
                onDelete()
            },
            onDismissRequest = {
                showDialog = false
            }
        )
    }

    Card(
        modifier = Modifier
            .then(
                if (onSelected != null) Modifier.clickable { onSelected() } else Modifier
            )
            .fillMaxWidth()
            .padding(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) Color.Gray else Color.White
        ),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.Center
            ) {
                BasicText(
                    modifier = Modifier.wrapContentSize(),
                    value = text,
                    fontSize = 16.sp,
                    isBold = true,
                )

                Row {
                    Spacer(modifier = Modifier.weight(1f))

                    TooltipIconButton(
                        tint = darkBlue,
                        icon = Icons.Default.Edit,
                        tooltip = editTitle,
                        function = {
                            showEditDialog = true
                        }
                    )

                    addSpaceHeight(4.dp)

                    TooltipIconButton(
                        tint = darkBlue,
                        icon = Icons.Default.Delete,
                        tooltip = getStringResource("info.delete"),
                        function = {
                            showDialog = true
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun addSpaceHeight(height: Dp = 5.dp) {
    Spacer(modifier = Modifier.height(height))
}