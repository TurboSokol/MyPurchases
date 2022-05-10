package com.turbosokol.mypurchases.android

import android.app.Application
import com.turbosokol.mypurchases.android.core.ReduxViewModel
import com.turbosokol.mypurchases.android.core.Service
import com.turbosokol.mypurchases.android.core.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.time.ExperimentalTime

@ExperimentalTime
class MyPurchasesApplication: Application(), KoinComponent {
    protected val viewModel: ReduxViewModel by inject()

    override fun onCreate() {
        super.onCreate()
        Service.applicationContext = this@MyPurchasesApplication
    }

    private fun initKoin() {
        com.turbosokol.mypurchases.core.di.initKoin {
            androidContext(this@MyPurchasesApplication)
            modules(viewModelModule)
        }
    }
}