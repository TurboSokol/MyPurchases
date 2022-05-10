package com.turbosokol.mypurchases.common.purchases.redux

import com.turbosokol.mypurchases.core.redux.Action
import com.turbosokol.mypurchases.core.redux.Reducer

class PurchaseReducer : Reducer<PurchaseState> {
    override fun reduce(oldState: PurchaseState, action: Action): PurchaseState {
        return when (action) {
            is PurchaseAction.AddPurchase -> {
                oldState.copy(
                    progress = false,
                    purchaseId = action.purchaseId,
                    parentListId = action.parentListId,
                    coast = action.coast,
                    description = action.description
                )
            }
            is PurchaseAction.EditPurchase -> {
                oldState.copy(
                    purchaseId = action.purchaseId,
                    parentListId = action.parentListId
                )
            }
            else -> oldState
        }
    }
}