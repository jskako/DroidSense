package notifications

data class ExtendedLog(
    val functions: Map<String, () -> Unit>? = null,
)
