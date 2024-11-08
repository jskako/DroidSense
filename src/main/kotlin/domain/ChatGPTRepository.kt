package domain

interface ChatGPTRepository {
    suspend fun getChatResponse(prompt: String): String
}