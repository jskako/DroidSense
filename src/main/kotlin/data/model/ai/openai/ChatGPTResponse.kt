package data.model.ai.openai

import kotlinx.serialization.Serializable

@Serializable
data class ChatGPTResponse(
    val id: String,
    val choices: List<Choice>,
    val usage: Usage
)