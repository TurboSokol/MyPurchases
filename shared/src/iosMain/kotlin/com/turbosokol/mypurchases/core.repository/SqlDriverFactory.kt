package com.turbosokol.mypurchases.core.repository

import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.drivers.native.NativeSqliteDriver
import com.turbosokol.mypurchases.SqlDatabase

actual class SqlDriverFactory {
    actual fun createDriver(): SqlDriver {
        return NativeSqliteDriver(SqlDatabase.Schema, "sqldatabase.db")
    }
}