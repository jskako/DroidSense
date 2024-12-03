package data.model.ai

import java.util.UUID

data class AIItem(
    val uuid: UUID,
    val deviceSerialNumber: String?,
    val aiType: AIType,
    val url: String,
    val model: String,
    val role: String,
    val message: String,
    val dateTime: String
)
