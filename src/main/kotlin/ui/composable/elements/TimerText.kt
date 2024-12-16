package ui.composable.elements

import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.delay

@Composable
fun TimerText(
    inProgress: Boolean
) {
    var elapsedSeconds by remember { mutableStateOf(0) }

    LaunchedEffect(inProgress) {
        while (inProgress) {
            delay(1000L)
            elapsedSeconds++
        }

        if (!inProgress) {
            elapsedSeconds = 0
        }
    }

    val hours = elapsedSeconds / 3600
    val minutes = (elapsedSeconds % 3600) / 60
    val seconds = elapsedSeconds % 60

    if(inProgress) {
        Text(
            text = String.format(TIME_FORMAT, hours, minutes, seconds),
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray
        )
    }
}

private const val TIME_FORMAT = "%02d:%02d:%02d"