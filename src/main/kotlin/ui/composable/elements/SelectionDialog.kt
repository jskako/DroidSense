package ui.composable.elements

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
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
import com.jskako.droidsense.generated.resources.Res
import com.jskako.droidsense.generated.resources.info_cancel
import com.jskako.droidsense.generated.resources.info_select_option
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import utils.Colors.darkBlue
import utils.Colors.lightGray

@Composable
fun SelectionDialog(
    title: StringResource = Res.string.info_select_option,
    options: List<String>,
    onDismissRequest: () -> Unit,
    onOptionSelected: (String) -> Unit
) {
    Dialog(onDismissRequest = { onDismissRequest() }) {
        Surface(
            shape = RoundedCornerShape(8.dp),
            color = Color.White
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(text = stringResource(title), fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Spacer(modifier = Modifier.height(8.dp))

                options.forEachIndexed { index, option ->
                    Text(
                        text = option,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onOptionSelected(option)
                                onDismissRequest()
                            }
                            .padding(12.dp),
                        fontSize = 16.sp
                    )

                    if (index != options.size - 1) {
                        HorizontalDivider()
                    }
                }

                Button(
                    onClick = { onDismissRequest() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = darkBlue,
                        disabledContainerColor = lightGray
                    ),
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text(stringResource(Res.string.info_cancel))
                }
            }
        }
    }
}