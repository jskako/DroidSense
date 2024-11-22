package ui.composable.elements.settings

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
import ui.composable.elements.iconButtons.TooltipIconButton
import ui.composable.elements.window.EditDialog
import utils.Colors.darkBlue
import utils.getStringResource

@Composable
fun DeleteEditRowCard(
    text: String,
    editTitle: String,
    onEdit: () -> Unit,
    onSelected: (() -> Unit)? = null,
    onDelete: () -> Unit,
    isSelected: Boolean = false
) {

    var showDialog by remember { mutableStateOf(false) }

    if (showDialog) {
        EditDialog(
            title = editTitle,
            text = text,
            onConfirmRequest = {
                showDialog = false
                onEdit()
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
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                BasicText(
                    value = text,
                    fontSize = 16.sp,
                    isBold = true,
                )

                Spacer(modifier = Modifier.weight(1f))

                TooltipIconButton(
                    tint = darkBlue,
                    icon = Icons.Default.Edit,
                    tooltip = editTitle,
                    function = {
                        showDialog = true
                    }
                )

                addSpaceHeight(4.dp)

                TooltipIconButton(
                    tint = darkBlue,
                    icon = Icons.Default.Delete,
                    tooltip = getStringResource("info.delete"),
                    function = onDelete
                )
            }
        }
    }
}

@Composable
private fun addSpaceHeight(height: Dp = 5.dp) {
    Spacer(modifier = Modifier.height(height))
}