package data.model.items

import java.util.UUID

data class AiNameItem(
    val sessionUuid: UUID,
    val name: String,
    val dateTime: String,
    val deviceSerialNumber: String?
)
