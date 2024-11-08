package data.model.ai

import kotlinx.serialization.Serializable

@Serializable
data class ChatGPTResponse(
    val choices: List<Choice>
)

@Serializable
data class Choice(
    val text: String
)