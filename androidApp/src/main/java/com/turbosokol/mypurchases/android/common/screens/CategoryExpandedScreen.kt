package com.turbosokol.mypurchases.android.common.screens

import androidx.compose.animation.core.ExperimentalTransitionApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.turbosokol.mypurchases.android.common.components.*
import com.turbosokol.mypurchases.android.common.theme.AppTheme
import com.turbosokol.mypurchases.android.common.theme.AppTheme.appSheetShape
import com.turbosokol.mypurchases.android.common.utils.deletePurchaseSafety
import com.turbosokol.mypurchases.android.common.utils.editPurchaseSafety
import com.turbosokol.mypurchases.android.core.ReduxViewModel
import com.turbosokol.mypurchases.common.app.AppState
import com.turbosokol.mypurchases.common.navigation.redux.AppTopBarStateType
import com.turbosokol.mypurchases.common.navigation.redux.ContentType
import com.turbosokol.mypurchases.common.navigation.redux.NavigationAction
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
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
    navController: NavController,
    categoryTitle: String
) {

    val stateFlow: StateFlow<AppState> = viewModel.store.observeAsState()
    val state by stateFlow.collectAsState(Dispatchers.Main)
    val categoriesState = state.getCategoriesState()
    val purchaseState = state.getPurchaseState()
    val navigationState = state.getNavigationState()

    var categoryPurchases by remember {
        mutableStateOf(purchaseState.categoryPurchases)
    }
    val expandableList = categoriesState.targetCategory


    val allCategoryItems = categoriesState.categoryItems
    val appTopBarStateType = navigationState.appTopBarStateType

    val keyboard = LocalSoftwareKeyboardController.current
    val scrollState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val bottomSheetState = rememberBottomSheetScaffoldState(
        bottomSheetState = BottomSheetState(BottomSheetValue.Collapsed)
    )

    BottomSheetScaffold(
        topBar = {
            AppTopBar(
                title = ("$categoryTitle ${expandableList.expectedSum}"),
                hasBackButton = true,
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
        sheetPeekHeight = 0.dp,
        sheetShape = appSheetShape,
        floatingActionButtonPosition = FabPosition.Center,
        floatingActionButton = {
            if (bottomSheetState.bottomSheetState.isCollapsed) {
                Row(
                    modifier = Modifier
                        .padding(start = AppTheme.appPaddingMedium8, end = AppTheme.appPaddingMedium8, bottom = 48.dp),
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    AddButton(
                        contentType = ContentType.Purchases
                    ) {
                        coroutineScope.launch {
                            bottomSheetState.bottomSheetState.expand()
                        }
                    }
                }
            }
        }
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LazyColumn(state = scrollState) {
                categoryPurchases = purchaseState.categoryPurchases
                itemsIndexed(categoryPurchases) { index, item ->
                    PurchaseColumnItem(
                        id = item.id,
                        parentTitle = item.parent,
                        coast = item.coast,
                        description = item.description ?: "",
                        keyboard = keyboard,
                        appTopBarStateType = appTopBarStateType,
                        onPurchaseModified = { id, parent, newCoast, description ->
                            //find editable category and edit coast values
                            editPurchaseSafety(
                                id = id,
                                parent = parent,
                                oldCoast = item.coast,
                                newCoast = newCoast,
                                description = description,
                                categoryItems = allCategoryItems
                            )
                            viewModel.execute(
                                NavigationAction.SwitchAppBarStateType(
                                    AppTopBarStateType.DEFAULT
                                )
                            )
                        },
                        onPurchaseDeleted = {
                            deletePurchaseSafety(
                                id = item.id,
                                parent = item.parent,
                                coast = item.coast,
                                allCategoryItems = allCategoryItems
                            )
                        }
                    )
                }
            }
        }
    }
}