package com.turbosokol.mypurchases.common.categories.redux

import com.turbosokol.mypurchases.common.app.AppState
import com.turbosokol.mypurchases.core.redux.Action
import com.turbosokol.mypurchases.core.redux.Effect
import com.turbosokol.mypurchases.core.redux.Middleware
import com.turbosokol.mypurchases.core.repository.local.MyPurchaseDAO
import comturbosokolmypurchases.CategoriesDb
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flow
import kotlin.time.ExperimentalTime

@ExperimentalTime
class CategoriesMiddleware(private val myPurchaseDAO: MyPurchaseDAO) : Middleware<AppState> {
    override suspend fun process(
        state: AppState,
        action: Action,
        sideEffect: MutableSharedFlow<Effect>
    ): Flow<Action> {
        return when (action) {
            is CategoriesAction.AddCategories -> flow {
                myPurchaseDAO.insertList(
                    title = action.title,
                    spentSum = action.spentSum,
                    expectedSum = action.expectedSum
                )
                emit(CategoriesAction.GetAllCategories)
            }

            is CategoriesAction.GetAllCategories -> flow {
                val data = myPurchaseDAO.getAllCategories()
                emit(CategoriesAction.SetCategories(data))
            }

            is CategoriesAction.GetCategory -> flow {
                val data = myPurchaseDAO.getListByTitle(action.categoryTitle) ?: CategoriesDb(
                    title = "error",
                    spentSum = 0,
                    expectedSum = 0
                )
                emit(CategoriesAction.SetExpandableCategory(data))
            }

            is CategoriesAction.DeleteAllCategories -> flow {
                myPurchaseDAO.deleteAllCategories()
                emit(CategoriesAction.GetAllCategories)
            }

            is CategoriesAction.DeleteListByTitle -> flow {
                myPurchaseDAO.deleteListByTitle(action.listTitle)
                emit(CategoriesAction.GetAllCategories)
            }
            else -> emptyFlow()
        }
    }
}