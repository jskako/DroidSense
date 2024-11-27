package data.model.ai

import kotlinx.serialization.Serializable

@Serializable
enum class AIType {
    OLLAMA, OPEN_API
}