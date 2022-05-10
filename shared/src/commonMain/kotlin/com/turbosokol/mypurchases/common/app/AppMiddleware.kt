package com.turbosokol.mypurchases.common.app

import com.turbosokol.mypurchases.core.redux.Action
import com.turbosokol.mypurchases.core.redux.Effect
import com.turbosokol.mypurchases.core.redux.Middleware
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlin.time.ExperimentalTime

@ExperimentalTime
class AppMiddleware(): Middleware<AppState> {
    override suspend fun process(
        state: AppState,
        action: Action,
        sideEffect: MutableSharedFlow<Effect>
    ): Flow<Action> {
        return super.process(state, action, sideEffect)
    }
}