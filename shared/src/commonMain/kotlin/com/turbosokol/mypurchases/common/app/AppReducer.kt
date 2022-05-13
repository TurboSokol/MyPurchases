package com.turbosokol.mypurchases.common.app

import com.turbosokol.mypurchases.core.redux.Action
import com.turbosokol.mypurchases.core.redux.Reducer
import kotlin.time.ExperimentalTime

@ExperimentalTime
class AppReducer: Reducer<AppState> {
    override fun reduce(oldState: AppState, action: Action): AppState {
        return oldState
    }
}