package com.turbosokol.mypurchases.android.common.screens

import android.annotation.SuppressLint
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.turbosokol.mypurchases.android.common.components.*
import com.turbosokol.mypurchases.android.common.theme.AppTheme.appPaddingMedium8
import com.turbosokol.mypurchases.android.common.utils.editCategory
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
    onCategoryClick: (Long) -> Unit
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
                title = MAIN_SCREEN_ROTE,
                hasBackButton = false,
                onBackClick = { navController.popBackStack() },
                hasOptionsButton = true,
                onOptionsClick = {
                    if (appTopBarStateType != AppTopBarStateType.DEFAULT) {
                        viewModel.execute(NavigationAction.CheckChanges(true))
                        viewModel.execute(NavigationAction.SwitchAppBarStateType(AppTopBarStateType.DEFAULT))
                    }
                    if (contentType == ContentType.CATEGORY) {
                        viewModel.execute(NavigationAction.SwitchMainScreenLook(ContentType.PURCHASE))
                    } else if (contentType == ContentType.PURCHASE) {
                        viewModel.execute(NavigationAction.SwitchMainScreenLook(ContentType.CATEGORY))
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
                ContentType.PURCHASE -> {
                    AddPurchaseContent(keyboard = keyboard)
                }
                ContentType.CATEGORY -> {
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
                        contentType = ContentType.PURCHASE
                    ) {
                        coroutineScope.launch {
                            bottomSheetState.bottomSheetState.expand()
                        }
                    }
                    Spacer(modifier = Modifier.weight(1F))
                    AddButton(
                        contentType = ContentType.CATEGORY
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
            if (contentType == ContentType.CATEGORY) {
                MainScreenCategoryContent(categoryItems, keyboard, appTopBarStateType,
                    onCategoryManage = { id, title, oldTitle, spentSum, expectSum ->
                        when (appTopBarStateType) {
                            AppTopBarStateType.EDIT -> {
                                viewModel.execute(
                                    PurchaseAction.GetAllPurchasesByParent(
                                        oldTitle
                                    )
                                )
                                val editablePurchases = purchasesState.purchaseItems
                                editCategory(
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
                                    CategoriesAction.DeleteCategoryByTitle(
                                        title
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
                    onCategoryClick = {
                        // IMPLEMENTED in navigation
                        onCategoryClick(it)
                    })
            } else {
                MainScreenPurchaseContent(purchaseItems, keyboard, appTopBarStateType,
                    onPurchaseDeleted = { id, parent, coast ->
                        var editableCategory: CategoriesDb? = null
                        categoryItems.forEach { category ->
                            if (category.title == parent) {
                                editableCategory = category
                            }
                        }

                        editableCategory?.let {
                            viewModel.execute(
                                CategoriesAction.InsertCategories(
                                    title = parent,
                                    spentSum = (it.spentSum - coast),
                                    expectedSum = it.expectedSum
                                )
                            )
                            viewModel.execute(PurchaseAction.DeletePurchaseById(id))
                        }
                    }, onPurchaseModified = { id, parent, oldCoast, newCoast, description ->
                        //find editable category and edit coast values
                        var editableCategory: CategoriesDb? = null
                        categoryItems.forEach { category ->
                            if (category.title == parent) {
                                editableCategory = category
                            }
                        }

                        editableCategory?.let { category ->
                            viewModel.execute(
                                CategoriesAction.EditCategories(
                                    id = category.id,
                                    title = parent,
                                    spentSum = (category.spentSum - oldCoast + newCoast),
                                    expectedSum = category.expectedSum
                                )
                            )
                            //Edit purchase
                            viewModel.execute(
                                PurchaseAction.EditPurchase(
                                    id = id,
                                    parentTitle = parent,
                                    coast = newCoast,
                                    description = description
                                )
                            )

                            viewModel.execute(
                                NavigationAction.SwitchAppBarStateType(
                                    AppTopBarStateType.DEFAULT
                                )
                            )
                        }
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
    categoryItems: List<CategoriesDb>,
    keyboard: SoftwareKeyboardController?,
    appTopBarStateType: AppTopBarStateType,
    onCategoryManage: (id: Long, title: String, oldTitle: String, spentSum: String, expectSum: String) -> Unit,
    onCategoryClick: (Long) -> Unit
) {
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
                text = "Spent Sum"
            )
            Text(
                modifier = Modifier.weight(0.25F),
                textAlign = TextAlign.Center,
                text = "Expect Sum"
            )
        }

        if (categoryItems.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Center) {
                CircularProgressIndicator()
            }
        } else {

            Row(verticalAlignment = Alignment.CenterVertically) {
                LazyColumn(state = scrollState) {
                    itemsIndexed(categoryItems) { index, item ->
                        CategoriesColumnItem(
                            title = item.title,
                            spentSum = item.spentSum,
                            expectedSum = item.expectedSum,
                            keyboard = keyboard,
                            appTopBarStateType = appTopBarStateType,
                            onCategoryManage = { title, spentSum, expectedSum ->
                                onCategoryManage(item.id, title, item.title, spentSum, expectedSum)
                            },
                            onCategoryClick = {
                                onCategoryClick(item.id)
                            }
                        )
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
    purchaseItems: List<PurchaseDb>,
    keyboard: SoftwareKeyboardController?,
    appTopBarStateType: AppTopBarStateType,
    onPurchaseDeleted: (id: Long, parent: String, coast: Double) -> Unit,
    onPurchaseModified: (id: Long, parent: String, oldCoast: Double, newCoast: Double, description: String?) -> Unit
) {
    val scrollState = rememberLazyListState()
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .padding(appPaddingMedium8),
            verticalAlignment = CenterVertically
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
                            description = item.description ?: "",
                            keyboard = keyboard,
                            appTopBarStateType = appTopBarStateType,
                            onPurchaseDeleted = {
                                onPurchaseDeleted(item.id, item.parent, item.coast)
                            },
                            onPurchaseModified = { coast, description ->
                                onPurchaseModified(
                                    item.id, item.parent, item.coast,
                                    coast, description
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}
