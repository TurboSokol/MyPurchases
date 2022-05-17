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
import com.turbosokol.mypurchases.android.common.screens.*
import com.turbosokol.mypurchases.android.core.ReduxViewModel
import com.turbosokol.mypurchases.common.app.AppState
import com.turbosokol.mypurchases.common.categories.redux.CategoriesAction
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
    val categoriesState = state.getCategoriesState()
    val startDestination = MAIN_SCREEN_ROTE

    val categoryExpandedRoute = "$CATEGORIES_EXPANDED_VIEW_ROUTE?$CATEGORY_TITLE={$CATEGORY_TITLE}"


    AnimatedNavHost(navController = navController, startDestination = startDestination) {
        composable(MAIN_SCREEN_ROTE) {
            MainScreen(navController = navController,
                onItemClick = { categoryTitle ->
                    viewModel.execute(CategoriesAction.GetCategory(categoryTitle))
                    val expandableList = categoriesState.expandableCategory
                    navController.navigate("$CATEGORIES_EXPANDED_VIEW_ROUTE/$categoryTitle=$it")
                })
        }

        composable(
            CATEGORIES_EXPANDED_VIEW_ROUTE, arguments = listOf(
            navArgument("title") {defaultValue = ""}
        )) { navBackstackEntry ->
            CategoryExpandedScreen(navController = navController, categoriesTitle = navBackstackEntry.arguments?.getString("title").orEmpty(), onItemClick = {

            })
        }
    }
}