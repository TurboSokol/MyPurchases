package com.turbosokol.mypurchases.common.purchases.redux

import com.turbosokol.mypurchases.core.redux.Action
import com.turbosokol.mypurchases.core.redux.Reducer

class PurchaseReducer : Reducer<PurchaseState> {
    override fun reduce(oldState: PurchaseState, action: Action): PurchaseState {
        return when (action) {
            is PurchaseAction.SetEditablePurchase -> {
                oldState.copy(editablePurchase = action.editablePurchase)
            }

            is PurchaseAction.SetPurchases -> {
                oldState.copy(purchaseItems = action.purchaseItems)
            }
            else -> oldState
        }
    }
}