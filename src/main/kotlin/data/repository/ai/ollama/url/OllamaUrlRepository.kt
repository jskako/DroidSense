package data.repository.ai.ollama.url

import kotlinx.coroutines.flow.Flow
import kotlin.coroutines.CoroutineContext

interface OllamaUrlRepository {

    suspend fun add(url: String)
    suspend fun update(url: String, value: String)
    fun get(context: CoroutineContext): Flow<List<String>>
    suspend fun deleteBy(url: String)
    suspend fun deleteAll()
}