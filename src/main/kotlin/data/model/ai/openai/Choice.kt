package data.model.ai.openai

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Choice(
    val index: Int,
    @SerialName("finish_reason") val finishReason: String,
    val message: Message
)
