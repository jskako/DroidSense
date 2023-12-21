package ui.composable.elements.iconButtons

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun IconButtonsColumn(
    icons: List<IconButtonsData>
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(2.dp)
    ) {
        icons.forEach {
            IconButton(
                onClick = {
                    it.function()
                },
                modifier = it.modifier
            ) {
                Icon(
                    imageVector = it.icon,
                    contentDescription = it.contentDescription,
                    tint = it.tint
                )
            }
        }
    }
}