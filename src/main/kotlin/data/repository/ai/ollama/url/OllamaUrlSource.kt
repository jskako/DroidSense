package data.repository.ai.ollama.url

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.jskako.OllamaUrlQueries
import kotlinx.coroutines.flow.Flow
import kotlin.coroutines.CoroutineContext

class OllamaUrlSource(
    private val urlDao: OllamaUrlQueries,
) : OllamaUrlRepository {

    override suspend fun add(url: String) = urlDao.insert(
        url = url
    )

    override suspend fun update(url: String, value: String) = urlDao.update(
        url = url,
        value = value
    )

    override fun get(context: CoroutineContext): Flow<List<String>> =
        urlDao.get().asFlow().mapToList(context)

    override suspend fun deleteBy(url: String) {
        urlDao.delete(url = url)
    }

    override suspend fun deleteAll() = urlDao.nukeTable()

}
