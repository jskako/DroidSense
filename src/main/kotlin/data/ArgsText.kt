package data

import org.jetbrains.compose.resources.StringResource

data class ArgsText(
    val textResId: StringResource? = null,
    val formatArgs: List<String>? = null
)