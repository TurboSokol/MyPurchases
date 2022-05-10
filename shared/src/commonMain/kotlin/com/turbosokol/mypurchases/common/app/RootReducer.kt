package com.turbosokol.mypurchases.common.app

import com.turbosokol.mypurchases.common.lists.redux.ListsReducer
import com.turbosokol.mypurchases.common.purchases.redux.PurchaseReducer
import com.turbosokol.mypurchases.core.redux.Action
import com.turbosokol.mypurchases.core.redux.Reducer
import kotlin.time.ExperimentalTime

@ExperimentalTime
class RootReducer(
    private val appReducer: AppReducer,
    private val listsReducer: ListsReducer,
    private val purchaseReducer: PurchaseReducer
): Reducer<AppState> {
    override fun reduce(oldState: AppState, action: Action): AppState =
        appReducer.reduce(oldState, action).copy(
            listsState = listsReducer.reduce(oldState.listsState, action),
            purchaseState = purchaseReducer.reduce(oldState.purchaseState, action)
        )
}