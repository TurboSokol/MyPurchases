package com.turbosokol.mypurchases.common.purchases.redux

import com.turbosokol.mypurchases.core.redux.Action
import com.turbosokol.mypurchases.core.redux.GeneralState

data class PurchaseState(
    val progress: Boolean,
    val purchaseId: Int? = null,
    val parentListId: Int,
    val coast: Int,
    val description: String? = null
) : GeneralState {
    companion object {
        fun getDefault(): PurchaseState = PurchaseState(
            progress = false,
            purchaseId = null,
            coast = 0,
            parentListId = 0
        )
    }
}

sealed class PurchaseAction: Action {
    data class AddPurchase(val purchaseId: Int, val parentListId: Int, val coast: Int, val description: String? = null): PurchaseAction()
    data class EditPurchase(val purchaseId: Int, val parentListId: Int): PurchaseAction()
}