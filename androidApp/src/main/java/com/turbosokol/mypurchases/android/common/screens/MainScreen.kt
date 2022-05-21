package com.turbosokol.mypurchases.android.common.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
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
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.turbosokol.mypurchases.android.R
import com.turbosokol.mypurchases.android.common.components.*
import com.turbosokol.mypurchases.android.common.theme.AppTheme
import com.turbosokol.mypurchases.android.core.ReduxViewModel
import com.turbosokol.mypurchases.common.app.AppState
import com.turbosokol.mypurchases.common.categories.redux.CategoriesAction
import com.turbosokol.mypurchases.common.navigation.redux.*
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
    val purchasesStateType = navigationState.purchasesStateType
    val categoriesStateType = navigationState.categoriesStateType

    val keyboard = LocalSoftwareKeyboardController.current
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
                hasBackButton = false,
                onBackClick = { navController.popBackStack() },
                hasOptionsButton = true,
                onOptionsClick = {
                    if (mainScreenLookType == MainScreenLookType.CATEGORIES) {
                        viewModel.execute(NavigationAction.SwitchMainScreenLook(MainScreenLookType.PURCHASES))
                    } else if (mainScreenLookType == MainScreenLookType.PURCHASES) {
                        viewModel.execute(NavigationAction.SwitchMainScreenLook(MainScreenLookType.CATEGORIES))
                    }
                },
                hasSubRightButton = true,
                subRightContentType = RightTopBarContentType.EDIT,
                onSubRightClick = {
                    when (mainScreenLookType) {
                        MainScreenLookType.PURCHASES -> {
                            if (purchasesStateType == PurchasesStateType.DEFAULT) {
                                viewModel.execute(NavigationAction.SwitchPurchaseStateType(PurchasesStateType.EDIT))
                            } else { viewModel.execute(NavigationAction.SwitchPurchaseStateType(PurchasesStateType.DEFAULT)) }
                        }
                        MainScreenLookType.CATEGORIES -> {
                            if (categoriesStateType == CategoriesStateType.DEFAULT) {
                                viewModel.execute(NavigationAction.SwitchCategoriesStateType(CategoriesStateType.EDIT))
                            } else { viewModel.execute(NavigationAction.SwitchCategoriesStateType(CategoriesStateType.DEFAULT)) }
                        }
                    }
                },
                hasRightButton = true,
                rightContentType = RightTopBarContentType.DELETE,
                onRightClick = { viewModel.execute(PurchaseAction.DeleteAllPurchases) })
        },
        sheetContent = {
            when (addButtonContentType) {
                AddButtonContentType.PURCHASE -> {
                    AddPurchaseContent(keyboard = keyboard)
                }
                AddButtonContentType.CATEGORY -> {
                    AddCategoryContent(keyboard = keyboard)
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
                    MainScreenCategoryContent(categoryItems, categoriesStateType, onCategoryClick)
                } else if (mainScreenLookType == MainScreenLookType.PURCHASES) {
                    MainScreenPurchaseContent(purchaseItems, purchasesStateType, onPurchaseClick)
                }
            }


        }
    }
}

@Composable
fun MainScreenCategoryContent(
    categoryItems: List<CategoriesDb>,
    categoriesStateType: CategoriesStateType,
    onCategoryClick: (String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            modifier = Modifier
                .padding(
                    start = AppTheme.appPaddingMedium,
                    end = AppTheme.appPaddingMedium,
                    top = AppTheme.appPaddingMedium
                )
                .border(AppTheme.appBorderStroke), verticalAlignment = CenterVertically
        ) {
            Text(modifier = Modifier.weight(0.5F), textAlign = TextAlign.Start, text = "Title")
            Text(modifier = Modifier.weight(0.25F), textAlign = TextAlign.Start, text = "Spent Sum")
            Text(
                modifier = Modifier.weight(0.25F),
                textAlign = TextAlign.Start,
                text = "Expect Sum"
            )
        }

        if (categoryItems.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Center) {
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
                            expectedSum = item.expectedSum,
                            categoriesStateType
                        ) {
                            onCategoryClick(item.title)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MainScreenPurchaseContent(
    purchaseItems: List<PurchaseDb>,
    purchasesStateType: PurchasesStateType,
    onPurchaseClick: (Long) -> Unit
) {
    val scrollState = rememberLazyListState()
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .padding(
                    start = AppTheme.appPaddingMedium,
                    end = AppTheme.appPaddingMedium,
                    top = AppTheme.appPaddingMedium
                )
                .border(AppTheme.appBorderStroke), verticalAlignment = CenterVertically
        ) {
            Text(modifier = Modifier.weight(0.4F), textAlign = TextAlign.Center, text = "Coast")
            Text(modifier = Modifier.weight(0.6F), textAlign = TextAlign.Center, text = "Title")
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            if (purchaseItems.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Center) {
                    CircularProgressIndicator()
                }
            } else {
                LazyColumn(state = scrollState) {
                    itemsIndexed(purchaseItems) { index, item ->
                        PurchaseColumnItem(
                            coast = item.coast,
                            title = item.description,
                            purchaseStateType = purchasesStateType
                        ) {
                            onPurchaseClick(item.id)
                        }
                    }
                }
            }
        }
    }
}

