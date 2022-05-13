package com.turbosokol.mypurchases.common.app

import com.turbosokol.mypurchases.common.lists.redux.ListsState
import com.turbosokol.mypurchases.common.purchases.redux.PurchaseState
import com.turbosokol.mypurchases.core.redux.Action
import com.turbosokol.mypurchases.core.redux.GeneralState
import kotlin.time.ExperimentalTime

@ExperimentalTime
data class AppState(
    internal val listsState: ListsState = ListsState.getDefault(),
    internal val purchaseState: PurchaseState = PurchaseState.getDefault()
) : GeneralState {
    fun getListsState() = listsState
    fun getPurchaseState() = purchaseState
}


