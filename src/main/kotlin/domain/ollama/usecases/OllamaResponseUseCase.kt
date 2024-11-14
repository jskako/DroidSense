package domain.ollama.usecases

import domain.ollama.OllamaRepository

class OllamaResponseUseCase(private val repository: OllamaRepository) {
    suspend operator fun invoke(model: String, prompt: String): Result<String> {
        return repository.getChatResponse(model, prompt)
    }
}