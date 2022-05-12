package com.turbosokol.mypurchases.core.di

import com.turbosokol.mypurchases.common.app.AppMiddleware
import com.turbosokol.mypurchases.common.app.AppState
import com.turbosokol.mypurchases.common.app.RootReducer
import com.turbosokol.mypurchases.common.lists.redux.ListsMiddleware
import com.turbosokol.mypurchases.common.purchases.redux.PurchaseMiddleware
import com.turbosokol.mypurchases.common.purchases.redux.PurchaseState
import com.turbosokol.mypurchases.core.redux.Action
import com.turbosokol.mypurchases.core.redux.Effect
import com.turbosokol.mypurchases.core.redux.ReduxStore
import com.turbosokol.mypurchases.core.redux.Store
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module
import kotlin.time.ExperimentalTime

@ExperimentalTime
fun initKoin(appDeclaration: KoinAppDeclaration = {}) = startKoin {
    appDeclaration()
    modules(storeModule)
}

val commonModule = module {

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
            defaultValue = AppState(get()),
            listOf(
                AppMiddleware(),
                ListsMiddleware(get()),
                PurchaseMiddleware(get())
            )
        )
    }
}