package di

import kotlinx.coroutines.*
import notifications.LogManager.addLog
import java.util.*

object AppModule {

    fun provideResourceBundle(baseName: String): ResourceBundle = ResourceBundle.getBundle(baseName)
    fun provideCoroutineScope() = CoroutineScope(
        CoroutineName(COROUTINE_NAME)
                + Dispatchers.Default
                + SupervisorJob()
                + CoroutineExceptionHandler { _, exception ->
            addLog(
                "CoroutineExceptionHandler: ${exception.message}"
            )
        }
    )
}

private const val COROUTINE_NAME = "DroidSense-Coroutine"