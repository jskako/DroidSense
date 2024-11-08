package ui.composable.elements

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import utils.Colors

@Composable
fun DividerColored(
    paddingValues: PaddingValues = PaddingValues(),
) {
    Box(
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxWidth()
            .height(2.dp)
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(Colors.darkBlue, Colors.darkRed)
                )
            )
    )
}