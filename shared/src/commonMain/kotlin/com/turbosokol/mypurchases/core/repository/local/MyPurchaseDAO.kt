package com.turbosokol.mypurchases.core.repository.local

import comturbosokolmypurchases.CategoriesDb
import comturbosokolmypurchases.PurchaseDb

interface MyPurchaseDAO {
    suspend fun getAllCategories(): List<CategoriesDb>
    suspend fun getListByTitle(title: String): CategoriesDb?
    suspend fun insertList(title: String, spentSum: Long, expectedSum: Long)
    suspend fun deleteAllCategories()
    suspend fun deleteListByTitle(title: String)

    suspend fun getAllPurchases(): List<PurchaseDb>
    suspend fun getAllPurchasesByParent(parentId: Long): List<PurchaseDb>
    suspend fun getPurchaseById(id: Long): PurchaseDb?
    suspend fun insertPurchase(id: Long? = null, parent: Long, coast: Long, title: String? = null)
    suspend fun deletePurchase(id: Long)
}