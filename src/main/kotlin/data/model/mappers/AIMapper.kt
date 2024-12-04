package data.model.mappers

import com.jskako.AIHistory
import data.model.ai.AIItem
import data.model.ai.AIType
import data.model.ai.ollama.AiRole
import data.model.ai.ollama.OllamaMessage
import java.util.UUID

fun AIHistory.toAiItem(): AIItem {
    return AIItem(
        uuid = UUID.fromString(this.uuid),
        deviceSerialNumber = this.deviceSerialNumber,
        aiType = AIType.valueOf(this.aiType),
        url = this.url,
        model = this.model,
        role = AiRole.fromString(this.role),
        message = this.message,
        dateTime = this.dateTime,
    )
}

fun AIItem.toOllamaMessage(): OllamaMessage {
    return OllamaMessage(
        role = this.role,
        content = this.message
    )
}