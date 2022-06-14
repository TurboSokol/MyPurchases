package com.turbosokol.mypurchases.android.common

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.ExperimentalTransitionApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.navigation.navArgument
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.turbosokol.mypurchases.android.common.screens.*
import com.turbosokol.mypurchases.android.core.ReduxViewModel
import com.turbosokol.mypurchases.common.app.AppState
import com.turbosokol.mypurchases.common.categories.redux.CategoriesAction
import com.turbosokol.mypurchases.common.purchases.redux.PurchaseAction
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import org.koin.androidx.compose.getViewModel
import kotlin.time.ExperimentalTime

@ExperimentalTransitionApi
@ExperimentalMaterial3Api
@ExperimentalComposeUiApi
@ExperimentalMaterialApi
@ExperimentalAnimationApi
@ExperimentalTime
@Composable
fun AppNavigation(viewModel: ReduxViewModel = getViewModel()) {
    val navController = rememberAnimatedNavController()
    val startDestination = MAIN_SCREEN_ROTE

    AnimatedNavHost(navController = navController, startDestination = startDestination) {
        composable(MAIN_SCREEN_ROTE) {
            MainScreen(
                navController = navController,
                onCategoryClick = { id, categoryTitle ->
                    viewModel.execute(PurchaseAction.GetAllPurchasesByParent(categoryTitle))
                    viewModel.execute(CategoriesAction.GetCategory(id))
                    navController.navigate("${CATEGORIES_EXPANDED_VIEW_ROUTE}/$categoryTitle")
                }
            )
        }

        composable(route = "$CATEGORIES_EXPANDED_VIEW_ROUTE/{categoryTitle}") { backStackEntry ->
            val categoryTitle = backStackEntry.arguments?.getString("categoryTitle") ?: ""
            CategoryExpandedScreen(navController = navController, categoryTitle = categoryTitle)

        }
    }
}