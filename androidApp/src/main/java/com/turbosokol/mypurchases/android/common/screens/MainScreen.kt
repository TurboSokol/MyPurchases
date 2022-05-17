package com.turbosokol.mypurchases.android.common.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Bottom
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.turbosokol.mypurchases.android.common.components.*
import com.turbosokol.mypurchases.android.core.ReduxViewModel
import com.turbosokol.mypurchases.common.app.AppState
import com.turbosokol.mypurchases.common.categories.redux.CategoriesAction
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getViewModel
import kotlin.time.ExperimentalTime

const val MAIN_SCREEN_ROTE = "Main Screen"

@SuppressLint("CoroutineCreationDuringComposition")
@ExperimentalMaterialApi
@ExperimentalTime
@Composable
fun MainScreen(
    viewModel: ReduxViewModel = getViewModel(),
    navController: NavController,
    lookStyle: MainScreenLookType = MainScreenLookType.CATEGORIES,
    onItemClick: (String) -> Unit
) {
    val stateFlow: StateFlow<AppState> = viewModel.store.observeAsState()
    val state by stateFlow.collectAsState(Dispatchers.Main)
    val categoriesState = state.getCategoriesState()
    val purchaseState = state.getPurchaseState()

    viewModel.execute(CategoriesAction.GetAllCategories)

    val scrollState = rememberLazyListState()
    val listItems = categoriesState.categoriesItems

    val coroutineScope = rememberCoroutineScope()
    val showAddSContent = purchaseState.showAddContent
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


    BottomSheetScaffold(
        topBar = {
            AppTopBar(
                title = MAIN_SCREEN_ROTE,
                onBackClick = {},
                hasOptionsButton = true,
                onOptionsClick = {},
                hasRightButton = true,
                onRightClick = {})
        },
        sheetContent = { AddPurchaseContent() },
        scaffoldState = bottomSheetState,
        sheetPeekHeight = 0.dp
    ) {
        Column() {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (listItems.isEmpty()) {
                        Text("Categories ARE EMPTY")
                    } else {
                        LazyColumn(state = scrollState) {
                            itemsIndexed(listItems) { index, item ->
                                CategoriesColumnItem(
                                    title = item.title,
                                    spentSum = item.spentSum,
                                    expectedSum = item.expectedSum
                                ) {
                                    onItemClick(item.title)
                                }
                            }
                        }
                    }
                }

                Row(
                    verticalAlignment = Bottom,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    AddButton(contentType = AddButtonContentType.PURCHASE)
                    AddButton(contentType = AddButtonContentType.CATEGORY)
                }
            }
        }
    }
}

enum class MainScreenLookType {
    CATEGORIES,
    PURCHASES
}
