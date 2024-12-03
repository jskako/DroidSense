package data.model.ai.ollama

import kotlinx.serialization.Serializable

@Serializable
data class OllamaMessage(
    val role: AiRole,
    val content: String
)