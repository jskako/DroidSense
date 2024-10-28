package data.repository.settings

import kotlinx.coroutines.flow.Flow

interface SettingsRepository {

    suspend fun add(identifier: String, value: String)
    suspend fun update(identifier: String, value: String)
    fun get(identifier: String): Flow<String>
    suspend fun delete(identifier: String)
    suspend fun isValid(): Boolean
}