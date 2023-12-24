package ui.composable.elements.iconButtons

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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
            val interactionSource = remember { MutableInteractionSource() }
            IconButton(
                onClick = { it.function() },
                modifier = it.modifier,
                interactionSource = interactionSource
            ) {
                Icon(
                    imageVector = it.icon,
                    contentDescription = it.contentDescription,
                    tint = it.tint
                )
            }
            val hovered by interactionSource.collectIsHoveredAsState()
            when (hovered) {
                true -> {
                    // TODO - Show hint
                }

                false -> {}
            }
        }
    }
}
