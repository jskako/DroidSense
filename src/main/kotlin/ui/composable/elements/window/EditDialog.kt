package ui.composable.elements.window

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.jskako.droidsense.generated.resources.Res
import com.jskako.droidsense.generated.resources.info_cancel
import com.jskako.droidsense.generated.resources.info_edit_name
import com.jskako.droidsense.generated.resources.info_save
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import utils.Colors.darkBlue
import utils.Colors.lightGray

@Composable
fun EditDialog(
    title: StringResource = Res.string.info_edit_name,
    text: String = "",
    onConfirmRequest: (String) -> Unit,
    onDismissRequest: () -> Unit,
    singleLine: Boolean = true,
) {

    var editText by remember { mutableStateOf(text) }

    Dialog(onDismissRequest = { onDismissRequest() }) {
        Surface(
            shape = RoundedCornerShape(8.dp),
            color = Color.White
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {

                Column(
                    modifier = Modifier.weight(1f),
                ) {
                    Text(
                        modifier = Modifier.padding(bottom = 8.dp),
                        text = stringResource(title),
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )

                    TextField(
                        value = editText,
                        singleLine = singleLine,
                        onValueChange = {
                            editText = it
                        },
                        placeholder = {
                            Text(stringResource(title))
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                }

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
                        Text(stringResource(Res.string.info_cancel))
                    }

                    Button(
                        onClick = { onConfirmRequest(editText) },
                        enabled = editText.trim().isNotEmpty(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = darkBlue,
                            disabledContainerColor = lightGray
                        )
                    ) {
                        Text(stringResource(Res.string.info_save))
                    }
                }
            }
        }
    }
}