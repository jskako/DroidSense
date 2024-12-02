package data.model.mappers

import com.jskako.AiName
import com.jskako.LogName
import data.model.items.AiNameItem
import data.model.items.LogNameItem
import java.util.UUID

fun LogName.toNameItem(): LogNameItem {
    return LogNameItem(
        sessionUuid = UUID.fromString(this.sessionUuid),
        name = this.name,
        dateTime = this.dateTime,
        deviceSerialNumber = this.deviceSerialNumber,
    )
}

fun AiName.toNameItem(): AiNameItem {
    return AiNameItem(
        sessionUuid = UUID.fromString(this.sessionUuid),
        name = this.name,
        dateTime = this.dateTime,
        deviceSerialNumber = this.deviceSerialNumber,
    )
}