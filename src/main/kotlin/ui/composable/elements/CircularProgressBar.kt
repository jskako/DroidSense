package ui.composable.elements

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import data.ArgsText
import org.jetbrains.compose.resources.stringResource
import utils.Colors.darkBlue

@Composable
fun CircularProgressBar(
    modifier: Modifier = Modifier.fillMaxSize(),
    text: ArgsText? = null,
    circularColor: Color = darkBlue,
    isVisible: Boolean = true
) {
    if (isVisible) {
        Column(
            modifier = modifier,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CircularProgressIndicator(
                modifier = Modifier.wrapContentSize(),
                color = circularColor,
            )
            text?.let {
                if (it.textResId == null) return@let
                Text(
                    text = if (it.formatArgs.isNullOrEmpty()) {
                        stringResource(it.textResId)
                    } else {
                        stringResource(
                            it.textResId,
                            *it.formatArgs.toTypedArray()
                        )
                    },
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }
        }
    }
}