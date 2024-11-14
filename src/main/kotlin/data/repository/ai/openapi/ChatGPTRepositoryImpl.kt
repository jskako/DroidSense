package data.repository.ai.openapi

import data.model.ai.openai.ChatGPTRequest
import data.model.ai.openai.ChatGPTResponse
import data.model.ai.openai.ErrorResponse
import data.model.ai.openai.Message
import data.network.AiModels.ChatGPT.GPT_3_5_TURBO
import data.network.HttpRoutes.OpenApi.OPEN_API_COMPLETIONS
import domain.openapi.ChatGPTRepository
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import io.ktor.http.headers
import kotlinx.serialization.json.Json

/**
 * IMPLEMENTATION EXAMPLE
 *
 * LaunchedEffect(Unit) {
 *         val httpClient = NetworkModule.provideHttpClient(
 *             settingsSource = sources.settingsSource,
 *         )
 *         val apiKey = sources.settingsSource.get(AIKey.CHATGPT.name)
 *         val chatGPTRepository = ChatGPTRepositoryImpl(httpClient, apiKey.first())
 *         val getChatResponseUseCase = GetChatResponseUseCase(chatGPTRepository)
 *         println("OpenAI answer: ${getChatResponseUseCase.invoke("Return me some sentence with 100 characters")}")
 *     }
 */

class ChatGPTRepositoryImpl(
    private val client: HttpClient,
    private val apiKey: String
) : ChatGPTRepository {

    override suspend fun getChatResponse(prompt: String): Result<String> {
        val requestBody = ChatGPTRequest(
            model = GPT_3_5_TURBO,
            messages = listOf(Message(role = "user", content = prompt)),
            maxTokens = 200
        )

        return runCatching {
            val response: HttpResponse = client.post(OPEN_API_COMPLETIONS) {
                headers {
                    append(HttpHeaders.Authorization, "Bearer $apiKey")
                    contentType(ContentType.Application.Json)
                }
                setBody(requestBody)
            }

            Json.decodeFromString<ChatGPTResponse>(response.bodyAsText())
                .choices.firstOrNull()?.message?.content ?: "No response"
        }.fold(
            onSuccess = { Result.success(it) },
            onFailure = {
                val errorMessage = it.message?.let { msg ->
                    Json.decodeFromString<ErrorResponse>(msg).error.message
                } ?: "Unknown error"
                Result.failure(Exception("Error: $errorMessage"))
            }
        )
    }
}