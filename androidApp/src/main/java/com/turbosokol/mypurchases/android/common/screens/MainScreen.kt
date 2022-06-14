package com.turbosokol.mypurchases.android.common.screens

import android.annotation.SuppressLint
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Bottom
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.turbosokol.mypurchases.android.R
import com.turbosokol.mypurchases.android.common.components.*
import com.turbosokol.mypurchases.android.common.theme.AppTheme.appPaddingMedium8
import com.turbosokol.mypurchases.android.common.theme.AppTheme.appSheetShape
import com.turbosokol.mypurchases.android.common.utils.deletePurchaseSafety
import com.turbosokol.mypurchases.android.common.utils.editCategorySafety
import com.turbosokol.mypurchases.android.common.utils.editPurchaseSafety
import com.turbosokol.mypurchases.android.core.ReduxViewModel
import com.turbosokol.mypurchases.common.app.AppState
import com.turbosokol.mypurchases.common.categories.redux.CategoriesAction
import com.turbosokol.mypurchases.common.navigation.redux.*
import com.turbosokol.mypurchases.common.purchases.redux.PurchaseAction
import comturbosokolmypurchases.PurchaseDb
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getViewModel
import kotlin.time.ExperimentalTime

const val MAIN_SCREEN_ROTE = "Main Screen"

@SuppressLint("CoroutineCreationDuringComposition")
@ExperimentalTransitionApi
@ExperimentalMaterial3Api
@ExperimentalComposeUiApi
@ExperimentalMaterialApi
@ExperimentalTime
@Composable
fun MainScreen(
    viewModel: ReduxViewModel = getViewModel(),
    navController: NavController,
    onCategoryClick: (categoryId: Long, categoryTitle: String) -> Unit
) {
    val stateFlow: StateFlow<AppState> = viewModel.store.observeAsState()
    val state by stateFlow.collectAsState(Dispatchers.Main)
    val categoriesState = state.getCategoriesState()
    val purchasesState = state.getPurchaseState()
    val navigationState = state.getNavigationState()

    viewModel.execute(CategoriesAction.GetAllCategories)
    viewModel.execute(PurchaseAction.GetAllPurchases)

    val allCategoryItems = categoriesState.categoryItems.reversed()
    val allPurchaseItems = purchasesState.purchaseItems

    val showAddSContent = navigationState.showAddContent
    val addButtonContentType = navigationState.addButtonType
    val contentType = navigationState.contentType
    val appTopBarStateType = navigationState.appTopBarStateType

    val keyboard = LocalSoftwareKeyboardController.current
    val coroutineScope = rememberCoroutineScope()
    val bottomSheetState =
        rememberBottomSheetScaffoldState(bottomSheetState = BottomSheetState(BottomSheetValue.Collapsed))

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
                title = contentType.toString(),
                hasBackButton = false,
                onBackClick = { navController.popBackStack() },
                hasOptionsButton = true,
                onOptionsClick = {
                    if (appTopBarStateType != AppTopBarStateType.DEFAULT) {
                        viewModel.execute(NavigationAction.SwitchAppBarStateType(AppTopBarStateType.DEFAULT))
                    }

                    if (contentType == ContentType.Categories) {
                        viewModel.execute(NavigationAction.SwitchMainScreenLook(ContentType.Purchases))
                    } else if (contentType == ContentType.Purchases) {
                        viewModel.execute(NavigationAction.SwitchMainScreenLook(ContentType.Categories))
                    }
                },
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
            when (addButtonContentType) {
                ContentType.Purchases -> {
                    AddPurchaseContent(keyboard = keyboard)
                }
                ContentType.Categories -> {
                    AddCategoryContent(keyboard = keyboard)
                }
            }
        },
        scaffoldState = bottomSheetState,
        sheetGesturesEnabled = true,
        sheetPeekHeight = 0.dp,
        sheetShape = appSheetShape,
        floatingActionButtonPosition = FabPosition.Center,
        floatingActionButton = {
            if (bottomSheetState.bottomSheetState.isCollapsed) {
                Row(
                    modifier = Modifier
                        .padding(start = appPaddingMedium8, end = appPaddingMedium8, bottom = 48.dp),
                    verticalAlignment = Bottom,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    AddButton(
                        contentType = ContentType.Purchases
                    ) {
                        coroutineScope.launch {
                            bottomSheetState.bottomSheetState.expand()
                        }
                    }
                    Spacer(modifier = Modifier.weight(1F))
                    AddButton(
                        contentType = ContentType.Categories
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
            if (contentType == ContentType.Categories) {
                MainScreenCategoryContent(
//                    allCategoryItems,
                    keyboard = keyboard, appTopBarStateType = appTopBarStateType,
                    onCategoryManage = { id, title, oldTitle, spentSum, expectSum ->
                        when (appTopBarStateType) {
                            AppTopBarStateType.EDIT -> {
                                viewModel.execute(
                                    PurchaseAction.GetAllPurchasesByParent(
                                        oldTitle
                                    )
                                )
                                val editablePurchases = purchasesState.purchaseItems
                                editCategorySafety(
                                    id, title, spentSum, expectSum, editablePurchases
                                )
                                viewModel.execute(
                                    NavigationAction.SwitchAppBarStateType(
                                        AppTopBarStateType.DEFAULT
                                    )
                                )
                                return@MainScreenCategoryContent
                            }
                            AppTopBarStateType.DELETE -> {
                                viewModel.execute(
                                    PurchaseAction.DeleteAllPurchasesByParent(
                                        title
                                    )
                                )
                                viewModel.execute(
                                    CategoriesAction.DeleteCategoryById(
                                        id
                                    )
                                )
                                viewModel.execute(
                                    NavigationAction.SwitchAppBarStateType(
                                        AppTopBarStateType.DEFAULT
                                    )
                                )

                            }
                            else -> {}
                        }
                    },
                    onCategoryClick = { categoryId, categoryTitle ->
                        // IMPLEMENTED in navigation
                        onCategoryClick(categoryId, categoryTitle)
                    })
            } else {
                MainScreenPurchaseContent(purchaseItems = allPurchaseItems, keyboard = keyboard, appTopBarStateType = appTopBarStateType,
                    onPurchaseDeleted = { id, parent, coast ->
                        deletePurchaseSafety(id = id, parent = parent, coast = coast, allCategoryItems = allCategoryItems)
                    }, onPurchaseModified = { id, parent, oldCoast, newCoast, description ->
                        //find editable category and edit coast values
                        editPurchaseSafety(
                            id = id,
                            parent = parent,
                            oldCoast = oldCoast,
                            newCoast = newCoast,
                            description = description ?: "",
                            categoryItems = allCategoryItems
                        )
                        viewModel.execute(NavigationAction.SwitchAppBarStateType(AppTopBarStateType.DEFAULT))
                    }
                )
            }
        }
    }
}


@ExperimentalTransitionApi
@ExperimentalTime
@ExperimentalComposeUiApi
@Composable
fun MainScreenCategoryContent(
    viewModel: ReduxViewModel = getViewModel(),
    keyboard: SoftwareKeyboardController?,
    appTopBarStateType: AppTopBarStateType,
    onCategoryManage: (id: Long, title: String, oldTitle: String, spentSum: String, expectSum: String) -> Unit,
    onCategoryClick: (categoryId: Long, categoryTitle: String) -> Unit
) {
    val stateFlow: StateFlow<AppState> = viewModel.store.observeAsState()
    val state by stateFlow.collectAsState(Dispatchers.Main)
    val categoryState = state.getCategoriesState()
    val categoryItems = categoryState.categoryItems

    val scrollState = rememberLazyListState()
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            modifier = Modifier
                .padding(appPaddingMedium8),
            verticalAlignment = CenterVertically
        ) {
            Text(modifier = Modifier.weight(0.5F), textAlign = TextAlign.Start, text = "Title")
            Text(
                modifier = Modifier.weight(0.25F),
                textAlign = TextAlign.Center,
                text = stringResource(R.string.spent_sum_text)
            )
            Text(
                modifier = Modifier.weight(0.25F),
                textAlign = TextAlign.Center,
                text = stringResource(R.string.expect_sum_text)
            )
        }

        if (categoryState.progress) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Center) {
                CircularProgressIndicator()
            }
        }
        else {
            if (categoryItems.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Center) {
                    Text(text = stringResource(R.string.add_new_category_hint))
                }
            } else {

                Row(verticalAlignment = Alignment.CenterVertically) {
                    LazyColumn(state = scrollState) {
                        itemsIndexed(categoryItems) { index, item ->
                            CategoriesColumnItem(
                                id = item.id,
                                title = item.title,
                                spentSum = item.spentSum,
                                expectedSum = item.expectedSum,
                                keyboard = keyboard,
                                appTopBarStateType = appTopBarStateType,
                                onCategoryManage = { title, spentSum, expectedSum ->
                                    onCategoryManage(item.id, title, item.title, spentSum, expectedSum)
                                },
                                onCategoryClick = {
                                    onCategoryClick(item.id, item.title)
                                }
                            )
                        }
                    }
                }
            }
        }

    }
}

@ExperimentalTime
@ExperimentalTransitionApi
@ExperimentalComposeUiApi
@Composable
fun MainScreenPurchaseContent(
    viewModel: ReduxViewModel = getViewModel(),
    purchaseItems: List<PurchaseDb>,
    keyboard: SoftwareKeyboardController?,
    appTopBarStateType: AppTopBarStateType,
    onPurchaseDeleted: (id: Long, parent: String, coast: Double) -> Unit,
    onPurchaseModified: (id: Long, parent: String, oldCoast: Double, newCoast: Double, description: String?) -> Unit
) {
    val stateFlow: StateFlow<AppState> = viewModel.store.observeAsState()
    val state by stateFlow.collectAsState(Dispatchers.Main)
    val purchaseState = state.getPurchaseState()

    val scrollState = rememberLazyListState()
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .padding(appPaddingMedium8),
            verticalAlignment = CenterVertically
        ) {
            Text(modifier = Modifier.weight(0.4F), textAlign = TextAlign.Center, text = stringResource(
                            R.string.coast_text)
                        )
            Text(modifier = Modifier.weight(0.6F), textAlign = TextAlign.Center, text = stringResource(
                            R.string.title_text)
                        )
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            if (purchaseState.progress) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Center) {
                    CircularProgressIndicator()
                }


            } else {
                if (purchaseItems.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Center) {
                        Text(text = stringResource(R.string.add_new_purchase_hint))
                    }
                } else {
                    LazyColumn(state = scrollState) {
                        itemsIndexed(purchaseItems) { index, item ->
                            PurchaseColumnItem(
                                id = item.id,
                                parentTitle = item.parent,
                                coast = item.coast,
                                description = item.description ?: "",
                                keyboard = keyboard,
                                appTopBarStateType = appTopBarStateType,
                                onPurchaseDeleted = {
                                    onPurchaseDeleted(item.id, item.parent, item.coast)
                                },
                                onPurchaseModified = { id, parent, newCoast, description ->
                                    onPurchaseModified(
                                        item.id, item.parent, item.coast,
                                        newCoast, description
                                    )
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
