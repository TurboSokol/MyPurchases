package com.turbosokol.mypurchases.core.repository.local

import com.squareup.sqldelight.db.SqlDriver
import com.turbosokol.mypurchases.SqlDatabase
import com.turbosokol.mypurchases.utils.appDispatcher
import comturbosokolmypurchases.CategoriesDb
import comturbosokolmypurchases.PurchaseDb
import kotlinx.coroutines.withContext

class SqlDataSourceImpl(sqlDriver: SqlDriver) : MyPurchaseDAO {
    private val dataBase = SqlDatabase(sqlDriver)
    private val queries = dataBase.tablesQueries

    //CATEGORIES
    override suspend fun getAllCategories(): List<CategoriesDb> {
        return queries.getCategoriesAll().executeAsList()
    }

    override suspend fun getCategoryById(id: Long): CategoriesDb? {
        return withContext(appDispatcher) {
            queries.getCategoriesById(id).executeAsOneOrNull()
        }
    }

    override suspend fun insertCategory(title: String, spentSum: Double, expectedSum: Double?) {
        return withContext(appDispatcher) {
            queries.insertCategory(
                title,
                spentSum,
                expectedSum ?: 0.0
            )
        }
    }

    override suspend fun editCategory(
        id: Long,
        title: String,
        spentSum: Double,
        expectedSum: Double?
    ) {
        return withContext(appDispatcher) {
            queries.editCategory(
                id,
                title,
                spentSum,
                expectedSum ?: 0.0
            )
        }
    }

    override suspend fun deleteAllCategories() {
        return withContext(appDispatcher) { queries.deleteAllCategories() }
    }

    override suspend fun deleteCategoryByTitle(title: String) {
        return withContext(appDispatcher) { queries.deleteCategoryByTitle(title) }
    }

    //PURCHASES
    override suspend fun getAllPurchases(): List<PurchaseDb> {
        return queries.getPurchaseAll().executeAsList()
    }

    override suspend fun getAllPurchasesByParent(parentTitle: String): List<PurchaseDb> {
        return queries.getAllPurchasesByParent(parentTitle).executeAsList()
    }

    override suspend fun getPurchaseById(id: Long): PurchaseDb? {
        return withContext(appDispatcher) { queries.getPurchaseById(id).executeAsOneOrNull() }
    }

    override suspend fun insertPurchase(parentTitle: String, coast: Double, description: String?) {
        return withContext(appDispatcher) {
            queries.insertPurchase(
                parent = parentTitle,
                coast = coast,
                description = description
            )
        }
    }

    override suspend fun editPurchase(
        id: Long,
        parentTitle: String,
        coast: Double,
        title: String?
    ) {
        return withContext(appDispatcher) { queries.editPurchase(id, parentTitle, coast, title) }
    }

    override suspend fun deletePurchaseById(id: Long) {
        return withContext(appDispatcher) { queries.deletePurchaseById(id) }
    }

    override suspend fun deleteAllPurchasesByParent(parentTitle: String) {
        return withContext(appDispatcher) {
            queries.deletePurchasesByParent(parentTitle)
        }
    }

    override suspend fun deleteAllPurchases() {
        queries.deleteAllPurchases()
    }
}