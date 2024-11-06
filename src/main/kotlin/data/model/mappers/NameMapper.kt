package data.model.mappers

import com.jskako.CustomName
import data.model.items.NameItem
import java.util.UUID

fun CustomName.toNameItem(): NameItem {
    return NameItem(
        uuid = UUID.fromString(this.uuid),
        name = this.name,
        deviceSerialNumber = this.deviceSerialNumber,
    )
}