package com.turbosokol.mypurchases.core.di

import com.russhwolf.settings.AndroidSettings
import com.russhwolf.settings.Settings
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver
import com.turbosokol.mypurchases.SqlDatabase
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformModule: Module = module {
    single<SqlDriver> {
        AndroidSqliteDriver(
            SqlDatabase.Schema,
            get(),
            "SqlDatabase"
        )
    }

    single<Settings> {
        AndroidSettings(get())
    }
}