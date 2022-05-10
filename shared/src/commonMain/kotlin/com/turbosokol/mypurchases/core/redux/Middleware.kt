package com.turbosokol.mypurchases.core.redux

import com.turbosokol.mypurchases.common.purchases.redux.PurchaseAction
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flow
import kotlin.time.ExperimentalTime

@ExperimentalTime
interface Middleware<T : GeneralState> {
    suspend fun process(
        state: T,
        action: Action,
        sideEffect: MutableSharedFlow<Effect>
    ): Flow<Action> =
        when (action) {
            is PurchaseAction.AddPurchase -> flow {

            }
            else -> { emptyFlow<Action>() }
        }
}