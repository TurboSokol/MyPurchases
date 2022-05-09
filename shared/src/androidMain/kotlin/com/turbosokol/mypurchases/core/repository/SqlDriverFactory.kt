package com.turbosokol.mypurchases.core.repository

import android.content.Context
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver
import com.turbosokol.mypurchases.SqlDatabase

actual class SqlDriverFactory(private val context: Context) {
    actual fun createDriver(): SqlDriver {
        return AndroidSqliteDriver(SqlDatabase.Schema, context, "sqldatabase.db")
    }
}