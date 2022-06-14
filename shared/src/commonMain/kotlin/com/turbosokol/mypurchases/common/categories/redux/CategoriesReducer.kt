package com.turbosokol.mypurchases.common.categories.redux

import com.turbosokol.mypurchases.core.redux.Action
import com.turbosokol.mypurchases.core.redux.Reducer

class CategoriesReducer : Reducer<CategoriesState> {
    override fun reduce(oldState: CategoriesState, action: Action): CategoriesState {
        return when (action) {
            is CategoriesAction.InsertCategories -> {
                oldState.copy(progress = true)
            }
            is CategoriesAction.EditCategories -> {
                oldState.copy(progress = true)
            }
            is CategoriesAction.GetAllCategories -> {
                oldState.copy(progress = true)
            }
            is CategoriesAction.GetCategory -> {
                oldState.copy(progress = true)
            }
            is CategoriesAction.DeleteAllCategories -> {
                oldState.copy(progress = true)
            }
            is CategoriesAction.DeleteCategoryById -> {
                oldState.copy(progress = true)
            }

            is CategoriesAction.SetTargetCategory -> {
                oldState.copy(progress = false, targetCategory = action.targetCategory)
            }

            is CategoriesAction.SetCategories -> {
                oldState.copy(progress = false, categoryItems = action.categoriesItems)
            }

            else -> oldState
        }
    }
}