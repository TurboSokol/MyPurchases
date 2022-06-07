package com.turbosokol.mypurchases.android.common.screens

import androidx.compose.animation.core.ExperimentalTransitionApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.turbosokol.mypurchases.android.common.components.AddPurchaseContent
import com.turbosokol.mypurchases.android.common.components.AppTopBar
import com.turbosokol.mypurchases.android.common.components.PurchaseColumnItem
import com.turbosokol.mypurchases.android.common.components.TopBarButtonsType
import com.turbosokol.mypurchases.android.common.utils.recalculatePurchase
import com.turbosokol.mypurchases.android.core.ReduxViewModel
import com.turbosokol.mypurchases.common.app.AppState
import com.turbosokol.mypurchases.common.categories.redux.CategoriesAction
import com.turbosokol.mypurchases.common.navigation.redux.AppTopBarStateType
import com.turbosokol.mypurchases.common.navigation.redux.NavigationAction
import com.turbosokol.mypurchases.common.purchases.redux.PurchaseAction
import comturbosokolmypurchases.CategoriesDb
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import org.koin.androidx.compose.getViewModel
import kotlin.time.ExperimentalTime

const val CATEGORIES_EXPANDED_VIEW_ROUTE = "Category Screen"

@ExperimentalTransitionApi
@ExperimentalComposeUiApi
@ExperimentalMaterial3Api
@ExperimentalMaterialApi
@ExperimentalTime
@Composable
fun CategoryExpandedScreen(
    viewModel: ReduxViewModel = getViewModel(),
    navController: NavController
) {
    val stateFlow: StateFlow<AppState> = viewModel.store.observeAsState()
    val state by stateFlow.collectAsState(Dispatchers.Main)
    val categoriesState = state.getCategoriesState()
    val purchaseState = state.getPurchaseState()
    val navigationState = state.getNavigationState()

    val expandableList = categoriesState.targetCategory
    val categoryItems = categoriesState.categoryItems
    val currentPurchasesList = purchaseState.purchaseItems
    val appTopBarStateType = navigationState.appTopBarStateType

    viewModel.execute(PurchaseAction.GetAllPurchasesByParent(expandableList.title))
    val keyboard = LocalSoftwareKeyboardController.current
    val scrollState = rememberLazyListState()
    val coroutineScope = rememberLazyListState()
    val bottomSheetState = rememberBottomSheetScaffoldState(
        bottomSheetState = BottomSheetState(BottomSheetValue.Collapsed)
    )

    BottomSheetScaffold(
        topBar = {
            AppTopBar(
                title = (expandableList.title + " " + expandableList.expectedSum.toString()),
                onBackClick = { navController.popBackStack() },
                hasOptionsButton = false,
                onOptionsClick = {},
                hasSubRightButton = true,
                subRightContentType = TopBarButtonsType.EDIT,
                onSubRightClick = {
                    if (appTopBarStateType != AppTopBarStateType.EDIT) {
                        viewModel.execute(NavigationAction.SwitchAppBarStateType(AppTopBarStateType.EDIT))
                    } else {
                        viewModel.execute(NavigationAction.SwitchAppBarStateType(AppTopBarStateType.DEFAULT))
                        viewModel.execute(NavigationAction.CheckChanges(true))
                    }
                },
                hasRightButton = true,
                rightContentType = TopBarButtonsType.DELETE,
                onRightClick = {
                    if (appTopBarStateType != AppTopBarStateType.DELETE) {
                        viewModel.execute(NavigationAction.SwitchAppBarStateType(AppTopBarStateType.DELETE))
                    } else {
                        viewModel.execute(NavigationAction.SwitchAppBarStateType(AppTopBarStateType.DEFAULT))
                    }
                })

        },
        sheetContent = {
            AddPurchaseContent(keyboard = keyboard)
        },
        scaffoldState = bottomSheetState,
        sheetPeekHeight = 0.dp
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LazyColumn(state = scrollState) {
                itemsIndexed(currentPurchasesList) { index, item ->
                    PurchaseColumnItem(
                        id = item.id,
                        parentTitle = item.parent,
                        coast = item.coast,
                        description = item.description ?: "",
                        keyboard = keyboard,
                        appTopBarStateType = appTopBarStateType,
                        onPurchaseModified = { id, parent, newCoast, description ->
                            //find editable category and edit coast values
                            recalculatePurchase(
                                id = id,
                                parent = parent,
                                oldCoast = item.coast,
                                newCoast = newCoast,
                                description = description,
                                categoryItems = categoryItems
                            )
                            viewModel.execute(
                                NavigationAction.SwitchAppBarStateType(
                                    AppTopBarStateType.DEFAULT
                                )
                            )
                        },
                        onPurchaseDeleted = {
                            var editableCategory: CategoriesDb? = null
                            categoryItems.forEach { category ->
                                if (category.title == item.parent) {
                                    editableCategory = category
                                }
                            }

                            editableCategory?.let {
                                viewModel.execute(
                                    CategoriesAction.EditCategories(
                                        id = it.id,
                                        title = it.title,
                                        spentSum = (it.spentSum - item.coast),
                                        expectedSum = it.expectedSum
                                    )
                                )
                                viewModel.execute(PurchaseAction.DeletePurchaseById(item.id))
                            }
                        }
                    )
                }

            }
        }
    }

}