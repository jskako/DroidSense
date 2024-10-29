package data

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.jskako.DSDatabase
import kotlin.io.path.Path
import kotlin.io.path.absolutePathString
import kotlin.io.path.exists


fun createDriver(): SqlDriver {
    val databasePath = Path(System.getProperty("user.home")).resolve(DATABASE_NAME)
    val driver = JdbcSqliteDriver(url = "jdbc:sqlite:${databasePath.absolutePathString()}")

    if (!databasePath.exists()) {
        DSDatabase.Schema.create(driver)
    }

    return driver
}

private const val DATABASE_NAME = "DSDatabase.db"
