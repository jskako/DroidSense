package data.model.items

import java.util.UUID

data class NameItem(
    val sessionUuid: UUID,
    val name: String,
    val dateTime: String,
    val deviceSerialNumber: String
) {
    companion object {
        val emptyNameItem = NameItem(
            sessionUuid = UUID(0, 0),
            name = "",
            dateTime = "",
            deviceSerialNumber = ""
        )
    }
}