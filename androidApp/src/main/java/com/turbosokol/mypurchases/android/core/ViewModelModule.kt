package com.turbosokol.mypurchases.android.core

import org.koin.dsl.module
import kotlin.time.ExperimentalTime

@ExperimentalTime
val viewModelModule = module {
    single { ReduxViewModel(get()) }
}