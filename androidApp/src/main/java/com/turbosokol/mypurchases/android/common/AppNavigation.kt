package com.turbosokol.mypurchases.android.common

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.navArgument
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.turbosokol.mypurchases.android.common.pages.*
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

    val listExpandedRoute = "$LIST_EXPANDED_VIEW_ROUTE?$LIST_ID={$LIST_ID}"


    AnimatedNavHost(navController = navController, startDestination = startDestination) {
        composable(LIST_PAGE_ROUTE) {
            ListsPage(navController = navController,
                onItemClick = { listId ->
                    viewModel.execute(ListsAction.GetList(listId))
                    val expandableList = listsState.expandableList
                    navController.navigate("$LIST_EXPANDED_VIEW_ROUTE?$listId=$it")
                })
        }

        composable(LIST_EXPANDED_VIEW_ROUTE, arguments = listOf(
            navArgument("listId") {defaultValue = ""}
        )) { navBackstackEntry ->
            ListExpandedPage(navController = navController, listId = navBackstackEntry.arguments?.getString("listId").orEmpty().toLong(), onItemClick = {

            })
        }
    }
}