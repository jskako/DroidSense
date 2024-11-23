package data.repository.ai.ollama.model

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.jskako.OllamaModelsQueries
import data.model.ai.ollama.OllamaModelItem
import kotlinx.coroutines.flow.Flow
import kotlin.coroutines.CoroutineContext

class OllamaModelSource(
    private val modelDao: OllamaModelsQueries,
) : OllamaModelRepository {

    override suspend fun add(ollamaModelItem: OllamaModelItem) = modelDao.insert(
        url = ollamaModelItem.url,
        model = ollamaModelItem.model,
        key = ollamaModelItem.key
    )

    override suspend fun update(url: String, model: String, value: String) = modelDao.update(
        url = url,
        model = model,
        value = value
    )

    override suspend fun updateUrls(url: String, value: String) = modelDao.updateUrls(
        url = url,
        value = value
    )

    override fun by(context: CoroutineContext, url: String): Flow<List<String>> =
        modelDao.getModels(url).asFlow().mapToList(context)

    override suspend fun deleteBy(url: String, model: String) {
        modelDao.delete(
            url = url,
            model = model
        )
    }

    override suspend fun deleteByUrl(url: String) {
        modelDao.deleteByUrl(url = url)
    }

    override suspend fun deleteAll() = modelDao.nukeTable()

}
