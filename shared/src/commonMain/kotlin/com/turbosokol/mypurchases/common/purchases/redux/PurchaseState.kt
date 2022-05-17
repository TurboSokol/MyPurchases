package com.turbosokol.mypurchases.common.purchases.redux

import com.turbosokol.mypurchases.common.purchases.model.PurchaseModel
import com.turbosokol.mypurchases.core.redux.Action
import com.turbosokol.mypurchases.core.redux.GeneralState
import comturbosokolmypurchases.PurchaseDb

data class PurchaseState(
    val progress: Boolean,
    val purchaseId: Long,
    val parentListId: Long,
    val coast: Long,
    val description: String? = null,
    val showAddContent: Boolean,
    val purchaseItems: List<PurchaseDb>,
    val editablePurchase: PurchaseDb
) : GeneralState {

//    fun getAllPurchases(): PurchaseModel = purchaseItems
//    fun getEditablePurchase(): PurchaseDb = editablePurchase

    companion object {
        fun getDefault(): PurchaseState = PurchaseState(
            progress = false,
            purchaseItems = emptyList(),
            purchaseId = 0,
            coast = 0,
            description = null,
            showAddContent = false,
            parentListId = 0,
            editablePurchase = PurchaseDb(id = 0, parent = 0, coast = 0, title = "")
        )
    }
}

sealed class PurchaseAction: Action {
    data class AddPurchase(val purchaseId: Long, val parentListId: Long, val coast: Long, val description: String? = null): PurchaseAction()
    object GetAllPurchases: PurchaseAction()
    data class GetAllPurchasesByParent(val parentId: Long): PurchaseAction()
    data class SetPurchases(val purchaseItems: List<PurchaseDb>): PurchaseAction()
    data class GetPurchase(val purchaseId: Long): PurchaseAction()
    data class SetEditablePurchase(val editablePurchase: PurchaseDb): PurchaseAction()
    data class ShowingAddContent(val showAddContent: Boolean, val contentType: String): PurchaseAction()
}