package ui.composable.sections

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import notifications.InfoManager.extendedInfo

@Composable
fun InfoSection(
    backgroundColor: Color = extendedInfo.value.color,
    text: String = extendedInfo.value.message
) {
    if (text.isNotEmpty()) {
        Column(
            modifier = Modifier
                .background(backgroundColor)
                .fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .background(backgroundColor)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                BasicTextField(
                    value = text,
                    onValueChange = { /*  */ },
                    textStyle = TextStyle(
                        color = Color.White,
                        fontSize = 16.sp
                    )
                )
            }
        }
    }
}