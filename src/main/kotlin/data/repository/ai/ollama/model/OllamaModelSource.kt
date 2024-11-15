package data.repository.ai.ollama.model

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.jskako.OllamaModelsQueries
import data.model.ai.ollama.OllamaModelItem
import data.model.mappers.toLogItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.coroutines.CoroutineContext

class OllamaModelSource(
    private val modelDao: OllamaModelsQueries,
) : OllamaModelRepository {

    override suspend fun add(ollamaModelItem: OllamaModelItem) = modelDao.insert(
        url = ollamaModelItem.url,
        model = ollamaModelItem.model
    )

    override fun by(context: CoroutineContext, url: String): Flow<List<String>> =
        modelDao.getModels(url).asFlow().mapToList(context)

    override suspend fun deleteBy(url: String, model: String) {
        modelDao.delete(
            url = url,
            model = model
        )
    }

    override suspend fun deleteAll() = modelDao.nukeTable()

}
