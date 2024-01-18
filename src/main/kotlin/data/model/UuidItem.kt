package data.model

import java.util.UUID

data class UuidItem(
    val uuid: UUID,
    val name: String,
    val time: String,
    val hasBeenRead: Boolean
)