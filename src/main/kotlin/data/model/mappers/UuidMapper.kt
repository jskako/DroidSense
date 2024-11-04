package data.model.mappers

import com.jskako.GetUUIDs
import data.model.items.UuidItem
import java.util.UUID

fun GetUUIDs.toUuidItem(): UuidItem {
    return UuidItem(
        uuid = UUID.fromString(this.uuid),
        time = this.time ?: ""
    )
}