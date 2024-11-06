package data.model.items

import java.util.UUID

data class NameItem(
    val uuid: UUID,
    val name: String,
    val deviceSerialNumber: String
) {
    companion object {
        val emptyNameItem = NameItem(
            uuid = UUID(0, 0),
            name = "",
            deviceSerialNumber = ""
        )
    }
}