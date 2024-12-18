package ui.composable.elements

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MarkdownText(
    modifier: Modifier = Modifier,
    text: String,
    fontSize: Int = 16
) {
    val styledText = parseMarkdownToAnnotatedString(text)
    Text(
        text = styledText,
        fontSize = fontSize.sp,
        modifier = modifier.padding(horizontal = 16.dp, vertical = 8.dp)
    )
}

fun parseMarkdownToAnnotatedString(text: String): AnnotatedString {
    return buildAnnotatedString {
        var cursor = 0
        val regex = """\*\*(.+?)\*\*|\*(.+?)\*|~~(.+?)~~|`(.+?)`""".toRegex()
        val matches = regex.findAll(text)

        for (match in matches) {
            val startIndex = match.range.first
            val endIndex = match.range.last

            if (cursor < startIndex) {
                append(text.substring(cursor, startIndex))
            }

            val boldGroup = match.groups[1]?.value
            val italicGroup = match.groups[2]?.value
            val strikeGroup = match.groups[3]?.value
            val codeGroup = match.groups[4]?.value

            when {
                boldGroup != null -> { // **bold**
                    pushStyle(SpanStyle(fontWeight = FontWeight.Bold))
                    append(boldGroup)
                    pop()
                }

                italicGroup != null -> { // *italic*
                    pushStyle(SpanStyle(fontStyle = FontStyle.Italic))
                    append(italicGroup)
                    pop()
                }

                strikeGroup != null -> { // ~~strikethrough~~
                    pushStyle(SpanStyle(textDecoration = TextDecoration.LineThrough))
                    append(strikeGroup)
                    pop()
                }

                codeGroup != null -> { // `code`
                    pushStyle(
                        SpanStyle(
                            background = Color.LightGray,
                            fontFamily = FontFamily.Monospace,
                            fontSize = 14.sp
                        )
                    )
                    append(codeGroup)
                    pop()
                }
            }

            cursor = endIndex + 1
        }

        if (cursor < text.length) {
            append(text.substring(cursor))
        }
    }
}