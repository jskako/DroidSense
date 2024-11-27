package data.model.mappers

import com.jskako.AIHistory
import data.model.ai.AIItem
import data.model.ai.AIType
import java.util.UUID

fun AIHistory.toAiItem(): AIItem {
    return AIItem(
        uuid = UUID.fromString(this.uuid),
        deviceSerialNumber = this.deviceSerialNumber,
        aiType = AIType.valueOf(this.aiType),
        url = this.url,
        model = this.model,
        role = this.role,
        message = this.message
    )
}