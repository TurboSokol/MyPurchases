package com.turbosokol.mypurchases.core.repository.local

import comturbosokolmypurchases.CategoriesDb
import comturbosokolmypurchases.PurchaseDb

interface MyPurchaseDAO {
    suspend fun getAllCategories(): List<CategoriesDb>
    suspend fun getCategoryById(id: Long): CategoriesDb?
    suspend fun insertCategory(title: String, spentSum: Double, expectedSum: Double?)
    suspend fun editCategory(id: Long, title: String, spentSum: Double, expectedSum: Double?)
    suspend fun deleteAllCategories()
    suspend fun deleteCategoryByTitle(title: String)

    suspend fun getAllPurchases(): List<PurchaseDb>
    suspend fun getAllPurchasesByParent(parentTitle: String): List<PurchaseDb>
    suspend fun getPurchaseById(id: Long): PurchaseDb?
    suspend fun insertPurchase(parentTitle: String, coast: Double, title: String? = null)
    suspend fun editPurchase(id: Long, parentTitle: String, coast: Double, title: String? = null)
    suspend fun deletePurchaseById(id: Long)
    suspend fun deleteAllPurchasesByParent(parentTitle: String)
    suspend fun deleteAllPurchases()
}