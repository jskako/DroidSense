package data.model.mappers

import com.jskako.CustomName
import data.model.items.NameItem
import java.util.UUID

fun CustomName.toNameItem(): NameItem {
    return NameItem(
        sessionUuid = UUID.fromString(this.sessionUuid),
        name = this.name,
        deviceSerialNumber = this.deviceSerialNumber,
    )
}