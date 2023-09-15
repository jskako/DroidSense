package ui.composable.elements.logs

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import logs.LogDetails
import ui.composable.elements.ButtonRow
import ui.composable.elements.CustomText
import utils.copyToClipboard
import utils.getStringResource
import utils.openFile

@Composable
fun LogCard(log: LogDetails) {
    Card(
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 8.dp)
            .fillMaxWidth(),
        elevation = 2.dp,
        backgroundColor = Color.Yellow,
        shape = RoundedCornerShape(corner = CornerSize(16.dp))
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            val extra = mutableMapOf<String, () -> Unit>()
            CustomText(
                text = log.time ?: "",
                fontSize = 22.sp,
                isBold = true
            )
            CustomText(
                text = log.log ?: ""
            )

            extra[getStringResource("info.log.card.copy")] = { log.log?.copyToClipboard() }

            log.documentPath.also {
                if (it.isNotEmpty()) {
                    extra[getStringResource("info.log.card.open")] = { openFile(it) }
                    extra[getStringResource("info.log.card.copy.path")] = { it.copyToClipboard() }
                }
            }

            log.extra.also {
                if (it != null) {
                    it.functions?.forEach { function ->
                        extra[function.key] = { function.value() }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            ButtonRow(extra)
        }
    }
}