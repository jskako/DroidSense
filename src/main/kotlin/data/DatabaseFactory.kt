package data

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.jskako.DSDatabase
import java.io.File

fun createDriver(): SqlDriver {
    val driver = JdbcSqliteDriver(url = "jdbc:sqlite:$DATABASE_NAME")
    if (!File(DATABASE_NAME).exists()) {
        DSDatabase.Schema.create(driver)
    }
    return driver
}

private const val DATABASE_NAME = "DSDatabase.db"
