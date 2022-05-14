package com.turbosokol.mypurchases.common.lists.redux

import com.turbosokol.mypurchases.common.lists.model.ListModel
import com.turbosokol.mypurchases.core.redux.Action
import com.turbosokol.mypurchases.core.redux.GeneralState
import comturbosokolmypurchases.ListsDb

data class ListsState(
    val progress: Boolean,
    val listId: Long? = null,
    val title: String,
    val spentSum: Long,
    val expectedSum: Long,
    val listItems: List<ListsDb>,
    val expandableList: ListsDb
) : GeneralState {

//    fun getAllLists(): ListModel = listItems
//    fun getExpandableList(): ListsDb = ListsState.

    companion object {
        fun getDefault(): ListsState {
            return ListsState(
                progress = true,
                listId = null,
                title = "",
                spentSum = 0,
                expectedSum = 0,
                listItems = emptyList(),
                expandableList = ListsDb(id = 0, title = "", spentSum = 0, expectedSum = 0)
            )
        }
    }
}

sealed class ListsAction : Action {
    data class AddLists(val listId: Long, val title: String, val spentSum: Long, val expectedSum: Long): ListsAction()
    object GetAllLists : ListsAction()
    data class SetLists(val listItems: List<ListsDb>) : ListsAction()
    data class GetList(val listId: Long) : ListsAction()
    data class SetExpandableList(val expandableList: ListsDb): ListsAction()

}