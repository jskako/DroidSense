package data.model.mappers

import com.jskako.DeviceInfo
import data.model.items.DeviceItem

fun DeviceInfo.toDeviceItem(): DeviceItem {
    return DeviceItem(
        serialNumber = this.serialNumber,
        model = this.model,
        manufacturer = this.manufacturer,
        brand = this.brand
    )
}