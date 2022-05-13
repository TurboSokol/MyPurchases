package com.turbosokol.mypurchases.android.common

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.turbosokol.mypurchases.android.common.pages.LIST_PAGE_ROUTE
import com.turbosokol.mypurchases.android.common.pages.LIST_EXPANDED_VIEW_ROUTE
import com.turbosokol.mypurchases.android.common.pages.ListExpandedPage
import com.turbosokol.mypurchases.android.common.pages.ListsPage
import com.turbosokol.mypurchases.android.core.ReduxViewModel
import com.turbosokol.mypurchases.common.app.AppState
import com.turbosokol.mypurchases.common.lists.redux.ListsAction
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import org.koin.androidx.compose.getViewModel
import kotlin.time.ExperimentalTime

@ExperimentalMaterialApi
@ExperimentalAnimationApi
@ExperimentalTime
@Composable
fun AppNavigation(viewModel: ReduxViewModel = getViewModel()) {
    val navController = rememberAnimatedNavController()
    val stateFlow: StateFlow<AppState> = viewModel.store.observeAsState()
    val state by stateFlow.collectAsState(Dispatchers.Main)
    val listsState = state.getListsState()
    val startDestination = LIST_PAGE_ROUTE

    AnimatedNavHost(navController = navController, startDestination = startDestination) {
        composable(LIST_PAGE_ROUTE) {
            ListsPage(navController = navController,
                onItemClick = { listId ->
                    viewModel.execute(ListsAction.GetList(listId))
                    val expandableList = listsState.expandableList
                    navController.navigate(LIST_EXPANDED_VIEW_ROUTE)
                })
        }

        composable(LIST_EXPANDED_VIEW_ROUTE) {
            ListExpandedPage()
        }
    }
}