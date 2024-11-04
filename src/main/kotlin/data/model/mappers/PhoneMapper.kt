package data.model.mappers

import com.jskako.PhoneInfo
import data.model.items.PhoneItem

fun PhoneInfo.toPhoneItem(): PhoneItem {
    return PhoneItem(
        serialNumber = this.serialNumber,
        model = this.model,
        manufacturer = this.manufacturer,
        brand = this.brand
    )
}