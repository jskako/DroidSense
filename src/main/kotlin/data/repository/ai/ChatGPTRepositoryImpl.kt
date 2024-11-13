package data.repository.ai

import data.model.ai.openai.ChatGPTRequest
import data.model.ai.openai.ChatGPTResponse
import data.model.ai.openai.ErrorResponse
import data.model.ai.openai.Message
import data.network.AiModels.ChatGPT.GPT_3_5_TURBO
import data.network.HttpRoutes.OPEN_API_COMPLETIONS
import domain.ChatGPTRepository
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

class ChatGPTRepositoryImpl(
    private val client: HttpClient,
    private val apiKey: String
) : ChatGPTRepository {

    override suspend fun getChatResponse(prompt: String): String {
        val requestBody = ChatGPTRequest(
            model = GPT_3_5_TURBO,
            messages = listOf(Message(role = "user", content = prompt)),
            maxTokens = 200
        )

        val response: HttpResponse = client.post(OPEN_API_COMPLETIONS) {
            headers {
                append(HttpHeaders.Authorization, "Bearer $apiKey")
                contentType(ContentType.Application.Json)
            }
            println("Headers before request: ${headers.entries()}")
            setBody(requestBody)
        }

        val rawResponse = response.bodyAsText()

        return runCatching {
            val chatResponse: ChatGPTResponse = Json.decodeFromString(rawResponse)
            chatResponse.choices.firstOrNull()?.message?.content ?: "No response"
        }.getOrElse {
            val errorResponse: ErrorResponse = Json.decodeFromString(rawResponse)
            "Error: ${errorResponse.error.message}"
        }
    }
}

/**
 * How to use example
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