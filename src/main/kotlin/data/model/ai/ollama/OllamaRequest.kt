package data.model.ai.ollama

import kotlinx.serialization.Serializable

@Serializable
data class OllamaRequest(
    val model: String,
    val messages: Array<OllamaMessage>,
    val stream: Boolean = false
)