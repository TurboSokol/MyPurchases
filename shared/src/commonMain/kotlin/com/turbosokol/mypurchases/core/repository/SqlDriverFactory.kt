package com.turbosokol.mypurchases.core.repository

import com.squareup.sqldelight.db.SqlDriver
import com.turbosokol.mypurchases.SqlDatabase

expect class SqlDriverFactory {
    fun createDriver(): SqlDriver
}

fun createDatabase(driverFactory: SqlDriverFactory): SqlDatabase = SqlDatabase(driverFactory.createDriver())