package com.turbosokol.mypurchases.common.categories.redux

import com.turbosokol.mypurchases.core.redux.Action
import com.turbosokol.mypurchases.core.redux.GeneralState
import comturbosokolmypurchases.CategoriesDb

data class CategoriesState(
    val progress: Boolean,
    val title: String,
    val spentSum: Long,
    val expectedSum: Long,
    val categoryItems: List<CategoriesDb>,
    val expandableCategory: CategoriesDb
) : GeneralState {

    companion object {
        fun getDefault(): CategoriesState {
            return CategoriesState(
                progress = true,
                title = "",
                spentSum = 0,
                expectedSum = 0,
                categoryItems = emptyList(),
                expandableCategory = CategoriesDb(title = "", spentSum = 0L, expectedSum = 0L)
            )
        }
    }
}

sealed class CategoriesAction : Action {
    data class AddCategories(val title: String, val spentSum: Long, val expectedSum: Long) :
        CategoriesAction()

    object GetAllCategories : CategoriesAction()
    data class SetCategories(val categoriesItems: List<CategoriesDb>) : CategoriesAction()
    data class GetCategory(val categoryTitle: String) : CategoriesAction()
    data class SetExpandableCategory(val expandableList: CategoriesDb) : CategoriesAction()
    object DeleteAllCategories : CategoriesAction()
    data class DeleteListByTitle(val listTitle: String) : CategoriesAction()

}