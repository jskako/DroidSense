package data.repository.ai.ollama

import data.model.ai.ollama.OllamaRequest
import data.model.ai.ollama.OllamaResponse
import domain.ollama.OllamaNetworkRepository
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.headers
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class OllamaNetworkRepositoryImpl(
    private val client: HttpClient,
    private val apiUrl: String
) : OllamaNetworkRepository {

    private val json = Json { encodeDefaults = true }

    override suspend fun getChatResponse(
        model: String,
        prompt: String
    ): Result<String> {
        val requestBody = json.encodeToString(OllamaRequest(model, prompt))

        return runCatching {
            val response: HttpResponse = client.post(apiUrl) {
                headers { contentType(ContentType.Application.Json) }
                setBody(requestBody)
            }

            json.decodeFromString<OllamaResponse>(response.bodyAsText()).response
        }.onFailure {
            it.message
        }
    }
}