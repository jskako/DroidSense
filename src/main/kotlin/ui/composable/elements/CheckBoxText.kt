package ui.composable.elements

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Checkbox
import androidx.compose.material.CheckboxDefaults
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import utils.Colors.darkBlue

@Composable
fun CheckboxBoxText(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    text: String,
    checkedState: Boolean,
    onChecked: (Boolean) -> Unit
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {

        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = if (checkedState) darkBlue else Color.Gray,
            modifier = Modifier
                .padding(end = 8.dp)
        )
        Text(
            text = text,
            style = TextStyle(
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = if (checkedState) Color.Black else Color.Gray
            )
        )

        Checkbox(
            checked = checkedState,
            onCheckedChange = { onChecked(!checkedState) },
            colors = CheckboxDefaults.colors(
                checkmarkColor = Color.Black,
                checkedColor = darkBlue,
                uncheckedColor = Color.Gray
            )
        )
    }
}