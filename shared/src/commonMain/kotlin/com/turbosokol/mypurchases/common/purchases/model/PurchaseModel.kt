package com.turbosokol.mypurchases.common.purchases.model

data class PurchaseModel(
    val purchaseId: Long,
    val parentListId: Long,
    val coast: Int,
    val description: String? = null
) {
    override fun toString(): String = """
        purchaseDB [
        id: $purchaseId
        parent: $parentListId
        coast: $coast
        title: $description
    """.trimIndent()
}
