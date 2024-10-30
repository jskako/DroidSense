package ui.composable.sections.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import utils.LICENSE_RESOURCE
import utils.readFile

@Composable
fun CopyrightSection() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 16.dp)
    ) {
        readFile(LICENSE_RESOURCE)?.let {
            BasicTextField(
                value = it,
                readOnly = true,
                onValueChange = { },
                textStyle = TextStyle(
                    color = Color.Gray
                )
            )
        }
    }
}