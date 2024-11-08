package data.model.mappers

import com.jskako.CustomName
import data.model.items.NameItem
import java.util.UUID

fun CustomName.toNameItem(): NameItem {
    return NameItem(
        sessionUuid = UUID.fromString(this.sessionUuid),
        name = this.name,
        dateTime = this.dateTime,
        deviceSerialNumber = this.deviceSerialNumber,
    )
}