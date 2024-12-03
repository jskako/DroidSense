package data.model.ai.ollama

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.Locale

@Serializable
enum class AiRole {
    @SerialName("system")
    SYSTEM,

    @SerialName("user")
    USER,

    @SerialName("assistant")
    ASSISTANT,

    @SerialName("tool")
    TOOL,

    @SerialName("unknown")
    UNKNOWN;

    fun databaseName(): String = name.lowercase(Locale.getDefault())

    companion object {
        fun fromString(name: String): AiRole {
            return entries.find { it.name == name.uppercase(Locale.getDefault()) } ?: UNKNOWN
        }
    }
}