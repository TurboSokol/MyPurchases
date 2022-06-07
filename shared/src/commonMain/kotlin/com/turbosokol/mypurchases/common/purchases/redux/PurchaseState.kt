package com.turbosokol.mypurchases.common.purchases.redux

import com.turbosokol.mypurchases.core.redux.Action
import com.turbosokol.mypurchases.core.redux.GeneralState
import comturbosokolmypurchases.PurchaseDb

data class PurchaseState(
    val progress: Boolean,
    val purchaseId: Double,
    val coast: Double,
    val description: String? = null,
    val purchaseItems: List<PurchaseDb>,
    val editablePurchase: PurchaseDb
) : GeneralState {

    companion object {
        fun getDefault(): PurchaseState = PurchaseState(
            progress = false,
            purchaseItems = emptyList(),
            purchaseId = 0.0,
            coast = 0.0,
            description = null,
            editablePurchase = PurchaseDb(id = 0L, parent = "", coast = 0.0, description = "")
        )
    }
}

sealed class PurchaseAction: Action {
    data class AddPurchase(val parentTitle: String, val coast: Double, val description: String? = null): PurchaseAction()
    data class EditPurchase(val id: Long, val parentTitle: String, val coast: Double, val description: String? = null): PurchaseAction()
    object GetAllPurchases: PurchaseAction()
    data class GetAllPurchasesByParent(val parentTitle: String): PurchaseAction()
    data class SetPurchases(val purchaseItems: List<PurchaseDb>): PurchaseAction()
    data class GetPurchase(val purchaseId: Long): PurchaseAction()
    data class SetEditablePurchase(val editablePurchase: PurchaseDb): PurchaseAction()
    data class DeletePurchaseById(val purchaseId: Long): PurchaseAction()
    data class DeleteAllPurchasesByParent(val parentTitle: String): PurchaseAction()
    object DeleteAllPurchases: PurchaseAction()
}