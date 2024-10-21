package ui.composable.elements.window

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import utils.Colors.darkBlue
import utils.Colors.lightGray
import utils.getStringResource

@Composable
fun TextDialog(
    title: String? = null,
    description: String,
    onConfirmRequest: () -> Unit,
    onDismissRequest: () -> Unit
) {
    Dialog(onDismissRequest = { onDismissRequest() }) {
        Surface(
            shape = RoundedCornerShape(8.dp),
            color = Color.White
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                title?.let {
                    Text(text = it, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                }

                Text(
                    text = description,
                    modifier = Modifier
                        .fillMaxWidth(),
                    fontSize = 16.sp
                )

                Row(
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .align(Alignment.End),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = { onDismissRequest() },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = darkBlue,
                            disabledContainerColor = lightGray
                        )
                    ) {
                        Text(getStringResource("info.cancel"))
                    }

                    Button(
                        onClick = { onConfirmRequest() },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = darkBlue,
                            disabledContainerColor = lightGray
                        )
                    ) {
                        Text(getStringResource("info.confirm"))
                    }
                }
            }
        }
    }
}