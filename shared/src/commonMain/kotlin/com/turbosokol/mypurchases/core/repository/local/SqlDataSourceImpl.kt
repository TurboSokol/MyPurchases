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

    override suspend fun getAllCategories(): List<CategoriesDb> {
        return queries.getCategoriesAll().executeAsList()
    }

    override suspend fun getListByTitle(title: String): CategoriesDb? {
        return withContext(appDispatcher) { queries.getCategoriesByTitle(title).executeAsOneOrNull() }
    }

    override suspend fun insertList(title: String, spentSum: Long, expectedSum: Long) {
        return withContext(appDispatcher) {
            queries.insertCategory(
                title,
                spentSum,
                expectedSum
            )
        }
    }

    override suspend fun deleteAllCategories() {
        return withContext(appDispatcher) { queries.deleteAllCategories() }
    }

    override suspend fun deleteListByTitle(title: String) {
        return withContext(appDispatcher) {queries.deleteListByTitle(title)}
    }

    override suspend fun getAllPurchases(): List<PurchaseDb> {
        return  queries.getPurchaseAll().executeAsList()
    }

    override suspend fun getAllPurchasesByParent(parentTitle: String): List<PurchaseDb> {
        return queries.getAllPurchasesByParent(parentTitle).executeAsList()
    }

    override suspend fun getPurchaseById(id: Long): PurchaseDb? {
        return withContext(appDispatcher) { queries.getPurchaseById(id).executeAsOneOrNull() }
    }

    override suspend fun insertPurchase(parentTitle: String, coast: Long, title: String?) {
        return withContext(appDispatcher) { queries.insertPurchase(parentTitle, coast, title) }
    }

    override suspend fun deletePurchase(id: Long) {
        return withContext(appDispatcher) { queries.deletePurchase(id) }
    }
}