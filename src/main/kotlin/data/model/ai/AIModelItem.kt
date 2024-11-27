package data.model.ai

import kotlinx.serialization.Serializable

@Serializable
data class AIModelItem(
    val url: String,
    val model: String,
    val aiType: AIType,
)