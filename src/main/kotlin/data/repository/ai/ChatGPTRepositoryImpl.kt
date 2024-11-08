package data.repository.ai

import data.model.ai.ChatGPTResponse
import domain.ChatGPTRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.headers

class ChatGPTRepositoryImpl(
    private val client: HttpClient,
    private val apiKey: String
) : ChatGPTRepository {

    override suspend fun getChatResponse(prompt: String): String {
        val response: ChatGPTResponse = client.post("https://api.openai.com/v1/completions") {
            headers {
                append("Authorization", "Bearer $apiKey")
                append("Content-Type", "application/json")
            }
            setBody(
                mapOf(
                    "model" to "text-davinci-003",
                    "prompt" to prompt,
                    "max_tokens" to 100
                )
            )
        }.body()

        return response.choices.firstOrNull()?.text ?: "No response"
    }
}