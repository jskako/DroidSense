package domain.openapi

interface ChatGPTRepository {
    suspend fun getChatResponse(prompt: String): Result<String>
}