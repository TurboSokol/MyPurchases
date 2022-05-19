package com.turbosokol.mypurchases.android.common.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Bottom
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.turbosokol.mypurchases.android.common.components.*
import com.turbosokol.mypurchases.android.core.ReduxViewModel
import com.turbosokol.mypurchases.common.app.AppState
import com.turbosokol.mypurchases.common.categories.redux.CategoriesAction
import com.turbosokol.mypurchases.common.navigation.redux.AddButtonContentType
import com.turbosokol.mypurchases.common.navigation.redux.MainScreenLookType
import com.turbosokol.mypurchases.common.navigation.redux.NavigationAction
import com.turbosokol.mypurchases.common.purchases.redux.PurchaseAction
import comturbosokolmypurchases.CategoriesDb
import comturbosokolmypurchases.PurchaseDb
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getViewModel
import kotlin.time.ExperimentalTime

const val MAIN_SCREEN_ROTE = "Main Screen"

@ExperimentalMaterial3Api
@ExperimentalComposeUiApi
@SuppressLint("CoroutineCreationDuringComposition")
@ExperimentalMaterialApi
@ExperimentalTime
@Composable
fun MainScreen(
    viewModel: ReduxViewModel = getViewModel(),
    navController: NavController,
    onCategoryClick: (String) -> Unit,
    onPurchaseClick: (Long) -> Unit
) {
    val stateFlow: StateFlow<AppState> = viewModel.store.observeAsState()
    val state by stateFlow.collectAsState(Dispatchers.Main)
    val categoriesState = state.getCategoriesState()
    val purchasesState = state.getPurchaseState()
    val navigationState = state.getNavigationState()

    viewModel.execute(CategoriesAction.GetAllCategories)
    viewModel.execute(PurchaseAction.GetAllPurchases)

    val categoryItems = categoriesState.categoryItems.reversed()
    val purchaseItems = purchasesState.purchaseItems

    val showAddSContent = navigationState.showAddContent
    val addButtonContentType = navigationState.addButtonType
    val mainScreenLookType = navigationState.mainScreenLookType

    val coroutineScope = rememberCoroutineScope()
    val bottomSheetState = rememberBottomSheetScaffoldState(
        bottomSheetState = BottomSheetState(BottomSheetValue.Collapsed)
    )

    if (showAddSContent) {
        coroutineScope.launch {
            bottomSheetState.bottomSheetState.expand()
        }
    } else {
        coroutineScope.launch {
            bottomSheetState.bottomSheetState.collapse()
        }
    }

    BottomSheetScaffold(modifier = Modifier.fillMaxSize(),
        topBar = {
            AppTopBar(
                title = MAIN_SCREEN_ROTE,
                onBackClick = { navController.popBackStack() },
                hasOptionsButton = true,
                onOptionsClick = {
                    if (mainScreenLookType == MainScreenLookType.CATEGORIES) {
                        viewModel.execute(NavigationAction.SwitchMainScreenLook(MainScreenLookType.PURCHASES))
                    } else if (mainScreenLookType == MainScreenLookType.PURCHASES) {
                        viewModel.execute(NavigationAction.SwitchMainScreenLook(MainScreenLookType.CATEGORIES))
                    }
                },
                hasRightButton = true,
                rightContentType = RightTopBarContentType.DELETE,
                onRightClick = { viewModel.execute(PurchaseAction.DeleteAllPurchases) })
        },
        sheetContent = {
            when (addButtonContentType) {
                AddButtonContentType.PURCHASE -> {
                    AddPurchaseContent()
                }
                AddButtonContentType.CATEGORY -> {
                    AddCategoryContent()
                }
            }

        },
        scaffoldState = bottomSheetState,
        sheetGesturesEnabled = true,
        sheetPeekHeight = 0.dp,
        sheetShape = RoundedCornerShape(8.dp),
        floatingActionButtonPosition = FabPosition.Center,
        floatingActionButton = {
            if (bottomSheetState.bottomSheetState.isCollapsed) {
                Row(
                    modifier = Modifier
                        .padding(start = 8.dp, end = 8.dp, bottom = 48.dp),
                    verticalAlignment = Bottom,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    AddButton(
                        contentType = AddButtonContentType.PURCHASE
                    ) {
                        coroutineScope.launch {
                            bottomSheetState.bottomSheetState.expand()
                        }
                    }
                    Spacer(modifier = Modifier.weight(1F))
                    AddButton(
                        contentType = AddButtonContentType.CATEGORY
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
            Column(
                modifier = Modifier,
                verticalArrangement = Arrangement.Center
            ) {
                if (mainScreenLookType == MainScreenLookType.CATEGORIES) {
                    MainScreenCategoryContent(categoryItems, onCategoryClick)
                } else if (mainScreenLookType == MainScreenLookType.PURCHASES) {
                    MainScreenPurchaseContent(purchaseItems, onPurchaseClick)
                }
            }


        }
    }
}

@Composable
fun MainScreenCategoryContent(
    categoryItems: List<CategoriesDb>,
    onCategoryClick: (String) -> Unit
) {
    if (categoryItems.isEmpty()) {
        Column(
            modifier = Modifier,
            horizontalAlignment = CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        val scrollState = rememberLazyListState()
        Row(verticalAlignment = Alignment.CenterVertically) {
            LazyColumn(state = scrollState) {
                itemsIndexed(categoryItems) { index, item ->
                    CategoriesColumnItem(
                        title = item.title,
                        spentSum = item.spentSum,
                        expectedSum = item.expectedSum
                    ) {
                        onCategoryClick(item.title)
                    }
                }
            }
        }
    }
}

@Composable
fun MainScreenPurchaseContent(purchaseItems: List<PurchaseDb>, onPurchaseClick: (Long) -> Unit) {
    val scrollState = rememberLazyListState()

    Row(verticalAlignment = Alignment.CenterVertically) {
        if (purchaseItems.isEmpty()) {
            Text("Purchases ARE EMPTY")
        } else {
            LazyColumn(state = scrollState) {
                itemsIndexed(purchaseItems) { index, item ->
                    PurchaseColumnItem(
                        coast = item.coast,
                        title = item.title
                    ) {
                        onPurchaseClick(item.id)
                    }
                }
            }
        }
    }
}



