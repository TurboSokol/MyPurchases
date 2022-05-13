package com.turbosokol.mypurchases.core.repository.local

import com.turbosokol.mypurchases.common.lists.model.ListModel
import com.turbosokol.mypurchases.common.purchases.model.PurchaseModel
import comturbosokolmypurchases.ListsDb
import comturbosokolmypurchases.PurchaseDb

interface MyPurchaseDAO {
    suspend fun getAllLists(): ListModel
    suspend fun getListById(id: Long): ListsDb?
    suspend fun insertList(id: Long? = null, title: String, spentSum: Long, expectedSum: Long)
    suspend fun deleteList(id: Long)

    suspend fun getAllPurchases(): List<PurchaseDb>
    suspend fun getPurchaseById(id: Long): PurchaseDb?
    suspend fun insertPurchase(id: Long? = null, parent: Long, coast: Long, title: String? = null)
    suspend fun deletePurchase(id: Long)
}