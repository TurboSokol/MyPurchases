package com.turbosokol.mypurchases.common.purchases.redux

import com.turbosokol.mypurchases.common.app.AppState
import com.turbosokol.mypurchases.common.categories.redux.CategoriesAction
import com.turbosokol.mypurchases.core.redux.Action
import com.turbosokol.mypurchases.core.redux.Effect
import com.turbosokol.mypurchases.core.redux.Middleware
import com.turbosokol.mypurchases.core.repository.local.MyPurchaseDAO
import comturbosokolmypurchases.PurchaseDb
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flow
import kotlin.time.ExperimentalTime

@ExperimentalTime
class PurchaseMiddleware(private val myPurchaseDAO: MyPurchaseDAO) : Middleware<AppState> {
    override suspend fun process(
        state: AppState,
        action: Action,
        sideEffect: MutableSharedFlow<Effect>
    ): Flow<Action> {
        return when (action) {
            is PurchaseAction.AddPurchase -> flow {
                myPurchaseDAO.insertPurchase(
                    parentTitle = action.parentTitle,
                    coast = action.coast,
                    title = action.description
                )
                emit(PurchaseAction.GetAllPurchases)
            }

            is PurchaseAction.EditPurchase -> flow {
                myPurchaseDAO.editPurchase(action.id, action.parentTitle, action.coast, action.description)
                emit(PurchaseAction.GetAllPurchases)
            }

            is PurchaseAction.GetAllPurchasesByParent -> flow {
                val data = myPurchaseDAO.getAllPurchasesByParent(action.parentTitle)
                emit(PurchaseAction.SetPurchases(data))
            }

            is PurchaseAction.GetAllPurchases -> flow {
                val data = myPurchaseDAO.getAllPurchases()
                emit(PurchaseAction.SetPurchases(data))
            }

            is PurchaseAction.GetPurchase -> flow {
                val data = myPurchaseDAO.getPurchaseById(action.purchaseId)
                data?.let {
                    emit(PurchaseAction.SetEditablePurchase(it))
                }

            }

            is PurchaseAction.DeletePurchaseById -> flow {
                myPurchaseDAO.deletePurchaseById(action.purchaseId)
                emit(PurchaseAction.GetAllPurchases)
            }

            is PurchaseAction.DeleteAllPurchasesByParent -> flow {
                myPurchaseDAO.deleteAllPurchasesByParent(action.parentTitle)
                emit(PurchaseAction.GetAllPurchases)
            }

            is PurchaseAction.DeleteAllPurchases -> flow {
                myPurchaseDAO.deleteAllPurchases()
                emit(PurchaseAction.GetAllPurchases)
            }

            else -> emptyFlow()
        }
    }
}