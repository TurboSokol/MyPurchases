package com.turbosokol.mypurchases.common.lists.redux

import com.turbosokol.mypurchases.common.lists.model.ListModel
import com.turbosokol.mypurchases.core.redux.Action
import com.turbosokol.mypurchases.core.redux.GeneralState

data class ListsState(
    val progress: Boolean,
    val listsFetched: Boolean,
    val listItems: List<ListModel>,
    val expandedList: ListModel? = null
) : GeneralState {
    companion object {
        fun getDefault(): ListsState {
            return ListsState(
                progress = true,
                listsFetched = false,
                listItems = listOf(
                    ListModel(
                        listId = 0,
                        title = "",
                        spentSum = 0,
                        expectedSum = 0
                    )
                )
            )
        }
    }
}

sealed class ListsAction : Action {
    data class GetLists(val progress: Boolean) : ListsAction()
    data class SetLists(val progress: Boolean, val listItems: List<ListModel>) : ListsAction()
    data class ExpandCurrentList(val listItem: ListModel) : ListsAction()
}