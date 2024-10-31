package ui.composable.elements.iconButtons

import androidx.compose.foundation.BasicTooltipBox
import androidx.compose.foundation.BasicTooltipState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupPositionProvider
import kotlinx.coroutines.launch
import utils.Colors.darkBlue

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun TooltipIconButton(
    modifier: Modifier = Modifier,
    iconSize: Dp = 36.dp,
    icon: ImageVector,
    tooltip: String,
    tint: Color = darkBlue,
    function: () -> Unit,
    isEnabled: Boolean = true
) {
    val scope = rememberCoroutineScope()
    val tooltipState = remember { BasicTooltipState() }
    val interactionSource = remember { MutableInteractionSource() }
    var tooltipVisible by remember { mutableStateOf(false) }

    val positionProvider = object : PopupPositionProvider {
        override fun calculatePosition(
            anchorBounds: IntRect,
            windowSize: IntSize,
            layoutDirection: LayoutDirection,
            popupContentSize: IntSize
        ): IntOffset {
            val x = anchorBounds.left
            val y = anchorBounds.bottom
            return IntOffset(x, y)
        }
    }

    BasicTooltipBox(
        tooltip = {
            Popup {
                Surface(
                    modifier = Modifier.padding(start = 4.dp),
                    color = darkBlue,
                    shape = MaterialTheme.shapes.small
                ) {
                    Text(
                        text = tooltip,
                        modifier = Modifier
                            .padding(8.dp),
                        color = Color.White
                    )
                }
            }
        },
        state = tooltipState,
        positionProvider = positionProvider
    ) {
        IconButton(
            enabled = isEnabled,
            onClick = {
                tooltipVisible = true
                function()
            },
            modifier = modifier
                .then(Modifier.size(iconSize)),
            interactionSource = interactionSource
        ) {
            Icon(
                imageVector = icon,
                contentDescription = tooltip,
                tint = tint
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
