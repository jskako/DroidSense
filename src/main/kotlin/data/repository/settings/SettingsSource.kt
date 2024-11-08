package data.repository.settings

import app.cash.sqldelight.coroutines.asFlow
import com.jskako.SettingsQueries
import data.keys.SettingsKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SettingsSource(private val settingsDao: SettingsQueries) :
    SettingsRepository {

    override suspend fun add(identifier: String, value: String) =
        settingsDao.insert(identifier = identifier, value_ = value)

    override suspend fun update(identifier: String, value: String) =
        settingsDao.update(identifier = identifier, value = value)

    override fun get(identifier: String): Flow<String> = settingsDao
        .get(identifier = identifier)
        .asFlow()
        .map { query ->
            query.executeAsOneOrNull() ?: ""
        }

    override suspend fun delete(identifier: String) = settingsDao.delete(identifier = identifier)

    override suspend fun isValid() =
        settingsDao.isValid(SettingsKey.ADB.name)
            .executeAsOneOrNull() ?: false && settingsDao.isValid(SettingsKey.SCRCPY.name)
            .executeAsOneOrNull() ?: false
}