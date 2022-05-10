package com.turbosokol.mypurchases.common.app

import com.turbosokol.mypurchases.common.lists.redux.ListsState
import com.turbosokol.mypurchases.common.purchases.redux.PurchaseState
import com.turbosokol.mypurchases.core.redux.Action
import com.turbosokol.mypurchases.core.redux.GeneralState
import kotlin.time.ExperimentalTime

@ExperimentalTime
data class AppState(
    val platform: AppPlatform = AppPlatform.NOT_SET,
    internal val listsState: ListsState = ListsState.getDefault(),
    internal val purchaseState: PurchaseState = PurchaseState.getDefault()
) : GeneralState {
    fun getListsState() = listsState
    fun getPurchaseState() = purchaseState
}

sealed class AppAction : Action {
    data class SetPlatform(val platform: AppPlatform) : AppAction()
}

enum class AppPlatform { ANDROID, IOS, NOT_SET }
