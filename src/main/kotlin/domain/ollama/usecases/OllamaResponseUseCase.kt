package domain.ollama.usecases

import domain.ollama.OllamaNetworkRepository

class OllamaResponseUseCase(private val repository: OllamaNetworkRepository) {
    suspend operator fun invoke(model: String, prompt: String): Result<String> {
        return repository.getChatResponse(model, prompt)
    }
}