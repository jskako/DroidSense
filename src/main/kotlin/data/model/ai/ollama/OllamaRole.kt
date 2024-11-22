package data.model.ai.ollama

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class OllamaRole {
    @SerialName("system")
    SYSTEM,

    @SerialName("user")
    USER,

    @SerialName("assistant")
    ASSISTANT,

    @SerialName("tool")
    TOOL
}