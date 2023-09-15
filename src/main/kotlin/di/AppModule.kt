package di

import adb.AdbManager
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import logs.InfoManager
import logs.LogManager
import ui.application.state.ProgressStateManager
import java.util.ResourceBundle

object AppModule {

    fun provideResourceBundle(baseName: String): ResourceBundle = ResourceBundle.getBundle(baseName)
    fun provideCoroutineScope() = CoroutineScope(
        CoroutineName(COROUTINE_NAME)
                + Dispatchers.Default
                + SupervisorJob()
                + CoroutineExceptionHandler { _, exception ->
            provideLogManager().addLog(
                "CoroutineExceptionHandler: ${exception.message}"
            )
        }
    )

    fun provideLogManager() = LogManager
    fun provideInfoManager() = InfoManager
    fun provideAdbManager() = AdbManager
    fun provideProgressStateManager() = ProgressStateManager
}

private const val COROUTINE_NAME = "DroidSense-Coroutine"