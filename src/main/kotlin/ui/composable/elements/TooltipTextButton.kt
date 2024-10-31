package ui.composable.elements

import androidx.compose.foundation.BasicTooltipBox
import androidx.compose.foundation.BasicTooltipState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import kotlinx.coroutines.launch
import ui.composable.utils.defaultPositionProvider
import utils.Colors.darkBlue

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun TooltipTextButton(
    modifier: Modifier = Modifier,
    tooltip: String,
    text: String,
    function: () -> Unit,
    isEnabled: Boolean = true
) {
    val scope = rememberCoroutineScope()
    val tooltipState = remember { BasicTooltipState() }
    val interactionSource = remember { MutableInteractionSource() }
    var tooltipVisible by remember { mutableStateOf(false) }

    BasicTooltipBox(
        tooltip = {
            Popup {
                Surface(
                    modifier = Modifier.padding(start = 4.dp),
                    color = darkBlue,
                    shape = MaterialTheme.shapes.small
                ) {
                    Text(
                        modifier = Modifier.padding(8.dp),
                        text = tooltip,
                        color = Color.White
                    )
                }
            }
        },
        state = tooltipState,
        positionProvider = defaultPositionProvider
    ) {

        TextButton(
            modifier = modifier,
            enabled = isEnabled,
            onClick = {
                tooltipVisible = true
                function()
            },
            interactionSource = interactionSource
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )
        }
    }

    val hovered by interactionSource.collectIsHoveredAsState()

    when (hovered) {
        true -> {
            scope.launch {
                if (!tooltipVisible) tooltipState.show()
            }
        }

        false -> {
            tooltipVisible = false
            tooltipState.dismiss()
        }
    }
}
