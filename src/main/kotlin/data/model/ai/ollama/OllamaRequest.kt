package data.model.ai.ollama

import kotlinx.serialization.Serializable

@Serializable
data class OllamaRequest(
    val model: String,
    val prompt: String,
    val stream: Boolean = false
)

