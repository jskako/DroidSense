package data.model.items

import utils.capitalizeFirstChar

data class DeviceItem(
    val serialNumber: String,
    val model: String?,
    val manufacturer: String?,
    val brand: String?
) {

    override fun toString(): String {
        return "${manufacturer?.capitalizeFirstChar()} $model"
    }

    companion object {
        val emptyDeviceItem = DeviceItem(
            serialNumber = "",
            model = null,
            manufacturer = null,
            brand = null
        )
    }
}
