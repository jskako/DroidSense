package data.model.ai

import data.model.ai.ollama.AiRole
import java.util.UUID

data class AIItem(
    val uuid: UUID,
    val deviceSerialNumber: String?,
    val aiType: AIType,
    val url: String,
    val model: String,
    val role: AiRole,
    val message: String,
    val dateTime: String
)
