package notifications.interfaces

import kotlinx.coroutines.CoroutineScope
import notifications.InfoManagerData

interface InfoManagerInterface {
    fun showMessage(
        infoManagerData: InfoManagerData,
        scope: CoroutineScope
    )

    fun clearInfoMessage()
}