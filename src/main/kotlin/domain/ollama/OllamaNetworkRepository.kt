package domain.ollama

import data.model.ai.ollama.OllamaMessage

interface OllamaNetworkRepository {
    suspend fun getChatResponse(model: String, messages: Array<OllamaMessage>): Result<OllamaMessage>
}