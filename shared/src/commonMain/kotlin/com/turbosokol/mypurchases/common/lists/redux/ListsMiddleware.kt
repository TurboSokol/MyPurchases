package com.turbosokol.mypurchases.common.lists.redux

import com.turbosokol.mypurchases.common.app.AppState
import com.turbosokol.mypurchases.core.redux.Action
import com.turbosokol.mypurchases.core.redux.Effect
import com.turbosokol.mypurchases.core.redux.Middleware
import com.turbosokol.mypurchases.core.repository.local.MyPurchaseDAO
import comturbosokolmypurchases.ListsDb
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flow
import kotlin.time.ExperimentalTime

@ExperimentalTime
class ListsMiddleware(private val myPurchaseDAO: MyPurchaseDAO): Middleware<AppState> {
    override suspend fun process(
        state: AppState,
        action: Action,
        sideEffect: MutableSharedFlow<Effect>
    ): Flow<Action> {
        return when (action) {
            is ListsAction.AddLists -> flow {
                myPurchaseDAO.insertList(action.listId, action.title, action.spentSum, action.expectedSum)
                emit(ListsAction.GetAllLists)
            }

            is ListsAction.GetAllLists -> flow {
                val data = myPurchaseDAO.getAllLists()
                emit(ListsAction.SetLists(data))
            }

            is ListsAction.GetList -> flow {
                val data = myPurchaseDAO.getListById(action.listId) ?: ListsDb(id = 0, title = "error", spentSum = 0, expectedSum = 0)
                emit(ListsAction.SetExpandableList(data))
            }
            else -> emptyFlow()
        }
    }
}