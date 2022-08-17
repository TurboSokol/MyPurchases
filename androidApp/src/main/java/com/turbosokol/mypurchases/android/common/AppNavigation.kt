package com.turbosokol.mypurchases.android.common

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.ExperimentalTransitionApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.turbosokol.mypurchases.android.common.screens.CATEGORIES_EXPANDED_VIEW_ROUTE
import com.turbosokol.mypurchases.android.common.screens.CategoryExpandedScreen
import com.turbosokol.mypurchases.android.common.screens.MAIN_SCREEN_ROTE
import com.turbosokol.mypurchases.android.common.screens.MainScreen
import com.turbosokol.mypurchases.android.core.ReduxViewModel
import com.turbosokol.mypurchases.common.categories.redux.CategoriesAction
import com.turbosokol.mypurchases.common.purchases.redux.PurchaseAction
import com.turbosokol.mypurchases.common.purchases.redux.PurchaseState
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
    val purchaseState = PurchaseState

    AnimatedNavHost(navController = navController, startDestination = startDestination) {
        composable(MAIN_SCREEN_ROTE) {
            MainScreen(
                navController = navController,
                onCategoryClick = {categoryId, categoryTitle->
                    navController.navigate("${CATEGORIES_EXPANDED_VIEW_ROUTE}/$categoryId/$categoryTitle")
                }
            )
        }

        composable(route = "$CATEGORIES_EXPANDED_VIEW_ROUTE/{categoryId}/{categoryTitle}") { backStackEntry ->
            val categoryId = backStackEntry.arguments?.getLong("categoryId") ?: 0L
            val categoryTitle = backStackEntry.arguments?.getString("categoryTitle") ?: ""
            CategoryExpandedScreen(navController = navController, categoryId = categoryId, categoryTitle = categoryTitle)

        }
    }
}