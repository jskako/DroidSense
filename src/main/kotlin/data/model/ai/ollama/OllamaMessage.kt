package data.model.ai.ollama

import kotlinx.serialization.Serializable

@Serializable
data class OllamaMessage(
    val role: OllamaRole,
    val content: String
)