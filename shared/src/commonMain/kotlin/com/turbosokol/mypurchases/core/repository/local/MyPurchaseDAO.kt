package com.turbosokol.mypurchases.core.repository.local

import comturbosokolmypurchases.ListsDb
import comturbosokolmypurchases.PurchaseDb
import kotlinx.coroutines.flow.Flow

interface MyPurchaseDAO {
    suspend fun getAllLists(): Flow<List<ListsDb>>
    suspend fun getListById(id: Long): ListsDb?
    suspend fun insertList(id: Long? = null, title: String, spentSum: Long, expectedSum: Long)
    suspend fun deleteList(id: Long)

    suspend fun getAllPurchases(): Flow<List<PurchaseDb>>
    suspend fun getPurchaseById(id: Long): PurchaseDb?
    suspend fun insertPurchase(id: Long? = null, parent: Long, coast: Long, title: String? = null)
    suspend fun deletePurchase(id: Long)
}