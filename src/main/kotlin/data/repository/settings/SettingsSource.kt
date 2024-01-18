package data.repository.settings

import com.jskako.SettingsQueries
import data.keys.SettingsKey

class SettingsSource(private val settingsDao: SettingsQueries) :
    SettingsRepository {

    override suspend fun add(identifier: String, value: String) =
        settingsDao.insert(identifier = identifier, value_ = value)

    override suspend fun update(identifier: String, value: String) =
        settingsDao.insert(identifier = identifier, value_ = value)

    override fun get(identifier: String): String = settingsDao.get(identifier = identifier).executeAsOne()
    override suspend fun delete(identifier: String) = settingsDao.delete(identifier = identifier)

    override suspend fun isValid() =
        settingsDao.isValid(SettingsKey.ADB.name)
            .executeAsOneOrNull() ?: false && settingsDao.isValid(SettingsKey.SCRCPY.name)
            .executeAsOneOrNull() ?: false
}