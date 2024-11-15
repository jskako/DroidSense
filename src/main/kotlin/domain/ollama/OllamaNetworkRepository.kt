package domain.ollama

interface OllamaNetworkRepository {
    suspend fun getChatResponse(model: String, prompt: String): Result<String>
}