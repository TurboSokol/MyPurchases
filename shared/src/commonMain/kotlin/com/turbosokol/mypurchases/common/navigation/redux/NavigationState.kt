package com.turbosokol.mypurchases.common.navigation.redux

import com.turbosokol.mypurchases.core.redux.Action
import com.turbosokol.mypurchases.core.redux.GeneralState

data class NavigationState(
    val addButtonType: AddButtonContentType,
    val showAddContent: Boolean,
    val mainScreenLookType: MainScreenLookType,
    val purchasesStateType: PurchasesStateType,
    val categoriesStateType: CategoriesStateType
) : GeneralState {

    companion object {
        fun getDefault(): NavigationState = NavigationState(
            addButtonType = AddButtonContentType.PURCHASE,
            showAddContent = false,
            mainScreenLookType = MainScreenLookType.CATEGORIES,
            purchasesStateType = PurchasesStateType.DEFAULT,
            categoriesStateType = CategoriesStateType.DEFAULT
        )
    }
}

sealed class NavigationAction : Action {
    data class ShowAddContent(val contentType: AddButtonContentType) : NavigationAction()
    data class HideAddContent(val showAddContent: Boolean = false): NavigationAction()
    data class SwitchMainScreenLook(val mainScreenLookType: MainScreenLookType): NavigationAction()
    data class SwitchPurchaseStateType(val purchasesStateType: PurchasesStateType): NavigationAction()
    data class SwitchCategoriesStateType(val categoriesStateType: CategoriesStateType): NavigationAction()
}

enum class AddButtonContentType {
    PURCHASE,
    CATEGORY
}

enum class MainScreenLookType {
    CATEGORIES,
    PURCHASES
}

enum class PurchasesStateType {
    DEFAULT,
    EDIT,
    DELETE
}

enum class CategoriesStateType {
    DEFAULT,
    EDIT,
    DELETE
}