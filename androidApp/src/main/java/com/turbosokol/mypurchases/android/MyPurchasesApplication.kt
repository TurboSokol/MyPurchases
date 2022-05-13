package com.turbosokol.mypurchases.android

import android.app.Application
import android.content.Context
import com.turbosokol.mypurchases.android.core.ReduxViewModel
import com.turbosokol.mypurchases.core.di.initKoin
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.compose.viewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.component.KoinComponent
import org.koin.dsl.module
import kotlin.time.ExperimentalTime

@ExperimentalTime
class MyPurchasesApplication: Application(), KoinComponent {

    override fun onCreate() {
        super.onCreate()
        initKoin(viewModelModule)
    }

    @ExperimentalTime
    val viewModelModule = module {
        single<Context> { this@MyPurchasesApplication }
        viewModel { ReduxViewModel(get()) }
    }

}