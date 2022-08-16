package com.turbosokol.mypurchases.common.purchases.redux

import com.turbosokol.mypurchases.core.redux.Action
import com.turbosokol.mypurchases.core.redux.Reducer

class PurchaseReducer : Reducer<PurchaseState> {
    override fun reduce(oldState: PurchaseState, action: Action): PurchaseState {
        return when (action) {
            is PurchaseAction.AddPurchase -> {
                oldState.copy(progress = true)
            }
            is PurchaseAction.EditPurchase -> {
                oldState.copy(progress = true)
            }
            is PurchaseAction.PurchaseProgressTrue -> {
                oldState.copy(progress = true)
            }
            is PurchaseAction.GetAllPurchases -> {
                oldState.copy(progress = true)
            }
            is PurchaseAction.GetEditablePurchase -> {
                oldState.copy(progress = true)
            }
            is PurchaseAction.DeletePurchaseById -> {
                oldState.copy(progress = true)
            }
            is PurchaseAction.DeleteAllPurchasesByParent -> {
                oldState.copy(progress = true)
            }
            is PurchaseAction.DeleteAllPurchases -> {
                oldState.copy(progress = true)
            }
            is PurchaseAction.SetEditablePurchase -> {
                oldState.copy(progress = false, editablePurchase = action.editablePurchase)
            }
            is PurchaseAction.SetCategoryPurchases -> {
                oldState.copy(progress = false, categoryPurchases = action.purchaseItems)
            }
            is PurchaseAction.SetPurchases -> {
                oldState.copy(progress = false, purchaseItems = action.purchaseItems)
            }
            else -> oldState
        }
    }
}