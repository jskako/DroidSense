package data

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.jskako.DSDatabase
import kotlin.io.path.Path
import kotlin.io.path.createDirectories
import kotlin.io.path.exists


fun createDriver(): SqlDriver {
    val droidSenseFolderPath = Path(System.getProperty("user.home"), "DroidSense")
    val databasePath = droidSenseFolderPath.resolve(DATABASE_NAME)

    if (!droidSenseFolderPath.exists()) {
        droidSenseFolderPath.createDirectories()
    }

    val driver = JdbcSqliteDriver(url = "jdbc:sqlite:${databasePath.toAbsolutePath()}")

    if (!databasePath.exists()) {
        DSDatabase.Schema.create(driver)
    }

    return driver
}

private const val DATABASE_NAME = "DSDatabase.db"