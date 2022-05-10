package com.turbosokol.mypurchases.common.lists.redux

import com.turbosokol.mypurchases.core.redux.Action
import com.turbosokol.mypurchases.core.redux.Reducer

class ListsReducer: Reducer<ListsState> {
    override fun reduce(oldState: ListsState, action: Action): ListsState {
        return when (action) {
            is ListsAction.GetLists -> {
                if (oldState.progress) {
                    oldState
                } else {
                    oldState.copy(progress = action.progress)
                }
            }
            is ListsAction.SetLists -> {
                oldState.copy(progress = false, listItems = action.listItems, listsFetched = true)
            }

            is ListsAction.ExpandCurrentList -> {
                oldState.copy(progress = false, expandedList = action.listItem)
            }

            else -> oldState
        }
    }
}