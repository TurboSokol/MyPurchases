package com.turbosokol.mypurchases.common.app

import com.turbosokol.mypurchases.common.categories.redux.CategoriesState
import com.turbosokol.mypurchases.common.purchases.redux.PurchaseAction
import com.turbosokol.mypurchases.common.purchases.redux.PurchaseState
import com.turbosokol.mypurchases.core.redux.GeneralState
import kotlin.time.ExperimentalTime

@ExperimentalTime
data class AppState(
    internal val categoriesState: CategoriesState = CategoriesState.getDefault(),
    internal val purchaseState: PurchaseState = PurchaseState.getDefault()
) : GeneralState {
    fun getCategoriesState() = categoriesState
    fun getPurchaseState() = purchaseState
}


