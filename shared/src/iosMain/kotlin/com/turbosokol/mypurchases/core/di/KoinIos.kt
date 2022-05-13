package com.turbosokol.mypurchases.core.di

import com.russhwolf.settings.AppleSettings
import com.russhwolf.settings.Settings
import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.drivers.native.NativeSqliteDriver
import com.turbosokol.mypurchases.SqlDatabase
import org.koin.core.KoinApplication
import org.koin.dsl.module
import platform.Foundation.NSUserDefaults
import kotlin.time.ExperimentalTime

@ExperimentalTime
fun initKoinIos(
    userDefaults: NSUserDefaults,
    doOnStartup: () -> Unit
): KoinApplication = initKoin(
    module {
        single<Settings> { AppleSettings(userDefaults) }
        single { doOnStartup }
    }
)

actual val platformModule = module {
    single<SqlDriver> { NativeSqliteDriver(SqlDatabase.Schema, "SqlDatabase") }
}