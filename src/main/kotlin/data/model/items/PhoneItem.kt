package data.model.items

import utils.capitalizeFirstChar

data class PhoneItem(
    val serialNumber: String,
    val model: String?,
    val manufacturer: String?,
    val brand: String?
) {

    override fun toString(): String {
        return "${manufacturer?.capitalizeFirstChar()} $model"
    }

    companion object {
        val emptyPhoneItem = PhoneItem(
            serialNumber = "",
            model = null,
            manufacturer = null,
            brand = null
        )
    }
}
