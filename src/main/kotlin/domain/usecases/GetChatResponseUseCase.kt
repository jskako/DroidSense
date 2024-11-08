package domain.usecases

import domain.ChatGPTRepository

class GetChatResponseUseCase(private val repository: ChatGPTRepository) {
    suspend operator fun invoke(prompt: String): String {
        return repository.getChatResponse(prompt)
    }
}