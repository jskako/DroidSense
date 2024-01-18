package data.repository.settings

interface SettingsRepository {

    suspend fun add(identifier: String, value: String)
    suspend fun update(identifier: String, value: String)
    fun get(identifier: String): String
    suspend fun delete(identifier: String)
    suspend fun isValid(): Boolean
}