package com.turbosokol.mypurchases.android.core

import androidx.lifecycle.ViewModel
import com.turbosokol.mypurchases.common.app.AppState
import com.turbosokol.mypurchases.core.redux.Action
import com.turbosokol.mypurchases.core.redux.Effect
import com.turbosokol.mypurchases.core.redux.Store
import kotlin.time.ExperimentalTime

@ExperimentalTime
class ReduxViewModel (
    val store: Store<AppState, Action, Effect>
): ViewModel() {
    fun execute(action: Action) {
        store.execute(action)
    }
}