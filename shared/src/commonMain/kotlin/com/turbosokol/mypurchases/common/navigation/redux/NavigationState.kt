package com.turbosokol.mypurchases.common.navigation.redux

import com.turbosokol.mypurchases.core.redux.Action
import com.turbosokol.mypurchases.core.redux.GeneralState

data class NavigationState(
    val addButtonType: AddButtonContentType,
    val showAddContent: Boolean,
    val mainScreenLookType: MainScreenLookType
) : GeneralState {

    companion object {
        fun getDefault(): NavigationState = NavigationState(
            addButtonType = AddButtonContentType.PURCHASE,
            showAddContent = false,
            mainScreenLookType = MainScreenLookType.CATEGORIES
        )
    }
}

sealed class NavigationAction : Action {
    data class ShowAddContent(val showAddContent: Boolean, val contentType: AddButtonContentType) : NavigationAction()
    data class HideAddContent(val showAddContent: Boolean = false): NavigationAction()
    data class SwitchMainScreenLook(val mainScreenLookType: MainScreenLookType): NavigationAction()
}

enum class AddButtonContentType {
    PURCHASE,
    CATEGORY
}

enum class MainScreenLookType {
    CATEGORIES,
    PURCHASES
}