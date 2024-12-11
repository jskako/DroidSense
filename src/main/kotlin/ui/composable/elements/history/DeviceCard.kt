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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jskako.droidsense.generated.resources.Res
import com.jskako.droidsense.generated.resources.info_delete
import com.jskako.droidsense.generated.resources.info_device_brand
import com.jskako.droidsense.generated.resources.info_serial_number
import data.model.items.DeviceItem
import notifications.InfoManagerData
import org.jetbrains.compose.resources.stringResource
import ui.composable.elements.BasicText
import ui.composable.elements.BasicTextCaption
import ui.composable.elements.iconButtons.TooltipIconButton
import utils.Colors.darkBlue
import utils.Colors.lightGray

@Composable
fun DeviceCard(
    deviceItem: DeviceItem,
    onClick: () -> Unit,
    onDelete: () -> Unit,
    deleteInProgress: Boolean,
    onDeleteInProgress: (Boolean) -> Unit,
    onMessage: (InfoManagerData) -> Unit
) {

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
                    value = deviceItem.toString(),
                    fontSize = 16.sp,
                    isBold = true,
                )

                Spacer(modifier = Modifier.weight(1f))

                TooltipIconButton(
                    isEnabled = !deleteInProgress,
                    tint = if (deleteInProgress) lightGray else darkBlue,
                    icon = Icons.Default.Delete,
                    tooltip = Res.string.info_delete,
                    function = {
                        onDeleteInProgress(true)
                        onDelete()
                    }
                )
            }

            addSpaceHeight(8.dp)

            BasicTextCaption(
                text1 = stringResource(Res.string.info_serial_number),
                text2 = deviceItem.serialNumber
            )

            deviceItem.brand?.let {
                addSpaceHeight(8.dp)

                BasicTextCaption(
                    text1 = stringResource(Res.string.info_device_brand),
                    text2 = it
                )
            }
        }
    }
}

@Composable
private fun addSpaceHeight(height: Dp = 5.dp) {
    Spacer(modifier = Modifier.height(height))
}