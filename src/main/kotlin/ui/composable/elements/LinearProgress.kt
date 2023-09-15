package ui.composable.elements

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun LinearProgress(
    modifier: Modifier = Modifier,
    isVisible: Boolean,
    color: Color = Color.Blue,
) {
    if (isVisible) {
        LinearProgressIndicator(
            modifier = modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            color = color
        )
    }
}