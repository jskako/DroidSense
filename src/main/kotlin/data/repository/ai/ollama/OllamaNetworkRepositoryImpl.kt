package data.repository.ai.ollama

import data.model.ai.ollama.OllamaMessage
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
import io.ktor.utils.io.CancellationException
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class OllamaNetworkRepositoryImpl(
    private val client: HttpClient,
    private val apiUrl: String = "http://localhost:11434/api/chat"
) : OllamaNetworkRepository {

    private val json = Json { encodeDefaults = true }

    override suspend fun getChatResponse(
        model: String,
        messages: Array<OllamaMessage>
    ): Result<OllamaMessage> {
        val requestBody = json.encodeToString(OllamaRequest(model, messages))

        return runCatching {
            val response: HttpResponse = client.post(apiUrl) {
                headers { contentType(ContentType.Application.Json) }
                setBody(requestBody)
            }

            json.decodeFromString<OllamaResponse>(response.bodyAsText()).message
        }.onFailure { throwable ->
            if (throwable is CancellationException) {
                throw throwable
            }
        }
    }
}