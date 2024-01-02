package ui.composable.elements.iconButtons

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.PlainTooltipBox
import androidx.compose.material3.PlainTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import utils.Colors.darkBlue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IconButtonsColumn(
    icons: List<IconButtonsData>
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(2.dp)
    ) {
        val scope = rememberCoroutineScope()
        icons.forEach {
            val tooltipState = remember { PlainTooltipState() }
            val interactionSource = remember { MutableInteractionSource() }

            PlainTooltipBox(
                modifier = Modifier.padding(start = 4.dp),
                tooltip = {
                    Text(
                        text = it.contentDescription,
                        color = Color.White
                    )
                },
                tooltipState = tooltipState,
                containerColor = darkBlue
            ) {
                IconButton(
                    onClick = {
                        it.function()
                    },
                    modifier = it.modifier,
                    interactionSource = interactionSource
                ) {
                    Icon(
                        imageVector = it.icon,
                        contentDescription = it.contentDescription,
                        tint = it.tint
                    )
                }
            }

            val hovered by interactionSource.collectIsHoveredAsState()
            when (hovered) {
                true -> {
                    scope.launch {
                        tooltipState.show()
                    }
                }

                false -> {
                    scope.launch {
                        tooltipState.dismiss()
                    }
                }
            }
        }
    }
}
