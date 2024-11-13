package data.model.ai.openai

import kotlinx.serialization.Serializable

@Serializable
data class ErrorDetail(
    val message: String,
    val type: String,
    val param: String? = null,
    val code: String? = null
)