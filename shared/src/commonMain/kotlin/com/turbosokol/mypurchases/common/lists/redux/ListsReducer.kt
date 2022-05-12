package com.turbosokol.mypurchases.common.lists.redux

import com.turbosokol.mypurchases.core.redux.Action
import com.turbosokol.mypurchases.core.redux.Reducer

class ListsReducer: Reducer<ListsState> {
    override fun reduce(oldState: ListsState, action: Action): ListsState {
        return when (action) {

            is ListsAction.SetLists -> {
                oldState.copy(progress = false, listItems = action.listItems)
            }

            is ListsAction.SetExpandableList -> {
                oldState.copy(progress = false, expandableList = action.expandableList)
            }
            else -> oldState
        }
    }
}