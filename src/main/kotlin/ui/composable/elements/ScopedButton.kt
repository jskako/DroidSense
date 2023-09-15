package ui.composable.elements

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import di.AppModule.provideCoroutineScope
import kotlinx.coroutines.launch

@Composable
fun ScopedButton(
    startPadding: Dp,
    endPadding: Dp,
    title: String,
    function: () -> Unit
) {
    val scope = provideCoroutineScope()
    Button(
        modifier = Modifier
            .padding(start = startPadding, end = endPadding),
        onClick = {
            scope.launch {
                function()
            }
        },
    ) {
        Text(title)
    }
}