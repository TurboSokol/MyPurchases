package com.turbosokol.mypurchases.common.navigation.redux

import com.turbosokol.mypurchases.core.redux.Action
import com.turbosokol.mypurchases.core.redux.GeneralState

data class NavigationState(
    val addButtonType: ContentType,
    val showAddContent: Boolean,
    val contentType: ContentType,
    val appTopBarStateType: AppTopBarStateType,
    val checkChanges: Boolean
) : GeneralState {

    companion object {
        fun getDefault(): NavigationState = NavigationState(
            addButtonType = ContentType.Purchases,
            showAddContent = false,
            contentType = ContentType.Categories,
            appTopBarStateType = AppTopBarStateType.DEFAULT,
            checkChanges = false
        )
    }
}

sealed class NavigationAction : Action {
    data class ShowAddContent(val contentType: ContentType) : NavigationAction()
    object HideAddContent: NavigationAction()
    data class SwitchMainScreenLook(val contentType: ContentType): NavigationAction()
    data class SwitchAppBarStateType(val appBarStateType: AppTopBarStateType): NavigationAction()
    data class CheckChanges(val flag: Boolean): NavigationAction()
}

enum class ContentType {
    Purchases,
    Categories
}

enum class AppTopBarStateType {
    DEFAULT,
    EDIT,
    DELETE
}