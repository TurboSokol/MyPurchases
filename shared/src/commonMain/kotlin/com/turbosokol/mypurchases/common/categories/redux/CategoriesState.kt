package com.turbosokol.mypurchases.common.categories.redux

import com.turbosokol.mypurchases.core.redux.Action
import com.turbosokol.mypurchases.core.redux.GeneralState
import comturbosokolmypurchases.CategoriesDb

data class CategoriesState(
    val progress: Boolean,
    val title: String,
    val spentSum: Double,
    val expectedSum: Double,
    val categoryItems: List<CategoriesDb>,
    val targetCategory: CategoriesDb
) : GeneralState {

    companion object {
        fun getDefault(): CategoriesState {
            return CategoriesState(
                progress = true,
                title = "",
                spentSum = 0.0,
                expectedSum = 0.0,
                categoryItems = emptyList(),
                targetCategory = CategoriesDb(id = 0, title = "", spentSum = 0.0, expectedSum = 0.0)
            )
        }
    }
}

sealed class CategoriesAction : Action {
    data class InsertCategories(val title: String, val spentSum: Double, val expectedSum: Double?) : CategoriesAction()
    data class EditCategories(val id: Long, val title: String, val spentSum: Double, val expectedSum: Double?) : CategoriesAction()
    object GetAllCategories : CategoriesAction()
    data class SetCategories(val categoriesItems: List<CategoriesDb>) : CategoriesAction()
    data class GetCategory(val categoryId: Long) : CategoriesAction()
    data class SetTargetCategory(val targetCategory: CategoriesDb) : CategoriesAction()
    object DeleteAllCategories : CategoriesAction()
    data class DeleteCategoryById(val id: Long) : CategoriesAction()
    object CategoryProgressTrue: CategoriesAction()
}