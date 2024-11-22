package data.model.ai.ollama

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OllamaResponse(
    val model: String,

    @SerialName("created_at")
    val createdAt: String,

    @SerialName("done_reason")
    val doneReason: String? = null,

    val message: OllamaMessage,

    val done: Boolean,

    @SerialName("total_duration")
    val totalDuration: Long? = null,

    @SerialName("load_duration")
    val loadDuration: Long? = null,

    @SerialName("prompt_eval_count")
    val promptEvalCount: Int? = null,

    @SerialName("prompt_eval_duration")
    val promptEvalDuration: Long? = null,

    @SerialName("eval_count")
    val evalCount: Int? = null,

    @SerialName("eval_duration")
    val evalDuration: Long? = null
)