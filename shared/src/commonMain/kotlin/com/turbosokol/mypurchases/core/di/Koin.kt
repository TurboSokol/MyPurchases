package com.turbosokol.mypurchases.core.di

import com.turbosokol.mypurchases.common.app.AppMiddleware
import com.turbosokol.mypurchases.common.app.AppReducer
import com.turbosokol.mypurchases.common.app.AppState
import com.turbosokol.mypurchases.common.app.RootReducer
import com.turbosokol.mypurchases.common.lists.redux.ListsMiddleware
import com.turbosokol.mypurchases.common.lists.redux.ListsReducer
import com.turbosokol.mypurchases.common.purchases.redux.PurchaseMiddleware
import com.turbosokol.mypurchases.common.purchases.redux.PurchaseReducer
import com.turbosokol.mypurchases.core.redux.Action
import com.turbosokol.mypurchases.core.redux.Effect
import com.turbosokol.mypurchases.core.redux.ReduxStore
import com.turbosokol.mypurchases.core.redux.Store
import com.turbosokol.mypurchases.core.repository.local.MyPurchaseDAO
import com.turbosokol.mypurchases.core.repository.local.SqlDataSourceImpl
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.module
import kotlin.time.ExperimentalTime

@ExperimentalTime
fun initKoin(appModule: Module) = startKoin {
    modules(appModule, storeModule, repositoryModule, platformModule)
}

@ExperimentalTime
val storeModule = module {
    single<Store<AppState, Action, Effect>> {
        ReduxStore(
            reducer = RootReducer(
                appReducer = get(),
                listsReducer = get(),
                purchaseReducer = get()
            ),
            defaultValue = AppState(),
            listOf(
                AppMiddleware(),
                ListsMiddleware(get()),
                PurchaseMiddleware(get())
            )
        )
    }

    single { AppReducer() }
    single { ListsReducer() }
    single { PurchaseReducer() }
}

val repositoryModule = module {
    single<MyPurchaseDAO> { SqlDataSourceImpl(get()) }
}

expect val platformModule: Module