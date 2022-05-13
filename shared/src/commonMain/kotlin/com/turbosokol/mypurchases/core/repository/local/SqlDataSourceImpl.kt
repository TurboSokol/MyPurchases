package com.turbosokol.mypurchases.core.repository.local

import com.squareup.sqldelight.db.SqlDriver
import com.turbosokol.mypurchases.SqlDatabase
import com.turbosokol.mypurchases.common.lists.model.ListModel
import com.turbosokol.mypurchases.common.purchases.model.PurchaseModel
import com.turbosokol.mypurchases.utils.appDispatcher
import comturbosokolmypurchases.ListsDb
import comturbosokolmypurchases.PurchaseDb
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext

class SqlDataSourceImpl(sqlDriver: SqlDriver) : MyPurchaseDAO {
    private val dataBase = SqlDatabase(sqlDriver)
    private val queries = dataBase.tablesQueries

    override suspend fun getAllLists(): ListModel {
        return queries.getListsAll().executeAsList() as ListModel
    }

    override suspend fun getListById(id: Long): ListsDb? {
        return withContext(appDispatcher) { queries.getListsById(id).executeAsOneOrNull() }
    }

    override suspend fun insertList(id: Long?, title: String, spentSum: Long, expectedSum: Long) {
        return withContext(appDispatcher) {
            queries.insertList(
                id,
                title,
                spentSum,
                expectedSum
            )
        }
    }

    override suspend fun deleteList(id: Long) {
        return withContext(appDispatcher) { queries.deleteList(id) }
    }

    override suspend fun getAllPurchases(): List<PurchaseDb> {
        return  queries.getPurchaseAll().executeAsList()
    }

    override suspend fun getPurchaseById(id: Long): PurchaseDb? {
        return withContext(appDispatcher) { queries.getPurchaseById(id).executeAsOneOrNull() }
    }

    override suspend fun insertPurchase(id: Long?, parent: Long, coast: Long, title: String?) {
        return withContext(appDispatcher) { queries.insertPurchase(id, parent, coast, title) }
    }

    override suspend fun deletePurchase(id: Long) {
        return withContext(appDispatcher) { queries.deletePurchase(id) }
    }
}