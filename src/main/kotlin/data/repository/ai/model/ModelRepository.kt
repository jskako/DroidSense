package data.repository.ai.model

import data.model.ai.AIModelItem
import kotlinx.coroutines.flow.Flow
import kotlin.coroutines.CoroutineContext

interface ModelRepository {

    suspend fun add(aiModelItem: AIModelItem)
    suspend fun update(url: String, model: String, value: String)
    suspend fun updateUrls(url: String, value: String)
    fun by(context: CoroutineContext, url: String): Flow<List<String>>
    fun types(context: CoroutineContext): Flow<List<String>>
    suspend fun deleteBy(url: String, model: String)
    suspend fun deleteByUrl(url: String)
    suspend fun deleteAll()
}