package com.turbosokol.mypurchases.common.app

import com.turbosokol.mypurchases.common.categories.redux.CategoriesReducer
import com.turbosokol.mypurchases.common.navigation.redux.NavigationReducer
import com.turbosokol.mypurchases.common.purchases.redux.PurchaseReducer
import com.turbosokol.mypurchases.core.redux.Action
import com.turbosokol.mypurchases.core.redux.Reducer
import kotlin.time.ExperimentalTime

@ExperimentalTime
class RootReducer(
    private val appReducer: AppReducer,
    private val categoriesReducer: CategoriesReducer,
    private val purchaseReducer: PurchaseReducer,
    private val navigationReducer: NavigationReducer
): Reducer<AppState> {
    override fun reduce(oldState: AppState, action: Action): AppState =
        appReducer.reduce(oldState, action).copy(
            categoriesState = categoriesReducer.reduce(oldState.categoriesState, action),
            purchaseState = purchaseReducer.reduce(oldState.purchaseState, action),
            navigationState = navigationReducer.reduce(oldState.navigationState, action)
        )
}