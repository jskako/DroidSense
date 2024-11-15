package data.repository.ai.ollama.model

import data.model.ai.ollama.OllamaModelItem
import kotlinx.coroutines.flow.Flow
import kotlin.coroutines.CoroutineContext

interface OllamaModelRepository {

    suspend fun add(ollamaModelItem: OllamaModelItem)
    fun by(context: CoroutineContext, url: String): Flow<List<String>>
    suspend fun deleteBy(url: String, model: String)
    suspend fun deleteAll()
}