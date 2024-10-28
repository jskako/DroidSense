package utils

import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

object Colors {

    val darkBlue = Color(0xFF3F51B5)
    val darkRed = Color(0xFF8B0000)
    val darkGreen = Color(0xFF006400)
    val lightGray = Color(0xFFD3D3D3)

    val transparentTextFieldDefault: TextFieldColors
        @Composable
        get() = TextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            disabledContainerColor = Color.Transparent,
            errorContainerColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            errorIndicatorColor = Color.Transparent
        )
}