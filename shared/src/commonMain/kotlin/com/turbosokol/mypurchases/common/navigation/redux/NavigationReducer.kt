package com.turbosokol.mypurchases.common.navigation.redux

import com.turbosokol.mypurchases.core.redux.Action
import com.turbosokol.mypurchases.core.redux.Reducer

class NavigationReducer: Reducer<NavigationState> {
    override fun reduce(oldState: NavigationState, action: Action): NavigationState {
        return when (action) {
            is NavigationAction.ShowAddContent -> {
                oldState.copy(addButtonType = action.contentType)
            }
            is NavigationAction.HideAddContent -> {
                oldState.copy(showAddContent = action.showAddContent)
            }
            is NavigationAction.SwitchMainScreenLook -> {
                oldState.copy(contentType = action.contentType)
            }
            is NavigationAction.SwitchAppBarStateType -> {
                oldState.copy(appTopBarStateType = action.appBarStateType)
            }
            is NavigationAction.CheckChanges -> {
                oldState.copy(checkChanges = action.flag)
            }
            else -> oldState
        }
    }
}