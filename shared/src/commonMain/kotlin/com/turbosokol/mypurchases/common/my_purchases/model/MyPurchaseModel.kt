package com.turbosokol.mypurchases.common.my_purchases.model

data class MyPurchaseModel(
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
