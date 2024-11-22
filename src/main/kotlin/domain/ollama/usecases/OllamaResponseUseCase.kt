package domain.ollama.usecases

import data.model.ai.ollama.OllamaMessage
import domain.ollama.OllamaNetworkRepository

class OllamaResponseUseCase(private val repository: OllamaNetworkRepository) {
    suspend operator fun invoke(model: String, messages: Array<OllamaMessage>): Result<OllamaMessage> {
        return repository.getChatResponse(model, messages)
    }
}