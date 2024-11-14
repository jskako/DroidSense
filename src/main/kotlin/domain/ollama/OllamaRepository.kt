package domain.ollama

interface OllamaRepository {
    suspend fun getChatResponse(model: String, prompt: String): Result<String>
}