package domain.openapi.usecases

import domain.openapi.ChatGPTRepository

class GetChatResponseUseCase(private val repository: ChatGPTRepository) {
    suspend operator fun invoke(prompt: String): Result<String> {
        return repository.getChatResponse(prompt)
    }
}