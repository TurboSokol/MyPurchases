package com.turbosokol.mypurchases.common.purchases.redux

import com.turbosokol.mypurchases.common.app.AppState
import com.turbosokol.mypurchases.common.purchases.model.PurchaseModel
import com.turbosokol.mypurchases.core.redux.Action
import com.turbosokol.mypurchases.core.redux.Effect
import com.turbosokol.mypurchases.core.redux.Middleware
import com.turbosokol.mypurchases.core.repository.local.MyPurchaseDAO
import comturbosokolmypurchases.PurchaseDb
import kotlinx.coroutines.flow.*
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
            is PurchaseAction.GetAllPurchasesByParent -> flow {
                val data = myPurchaseDAO.getAllPurchasesByParent(action.parentTitle)
                emit(PurchaseAction.SetPurchases(data))
            }

            is PurchaseAction.GetAllPurchases -> flow {
               val data = myPurchaseDAO.getAllPurchases()
                emit(PurchaseAction.SetPurchases(data))
            }

            is PurchaseAction.GetPurchase -> flow {
                val data = myPurchaseDAO.getPurchaseById(action.purchaseId) ?: PurchaseDb(id = 0, title = "error", coast = 0, parent = "")
                emit(PurchaseAction.SetEditablePurchase(data))
            }

            else -> emptyFlow()
        }
    }
}