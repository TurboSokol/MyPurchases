package com.turbosokol.mypurchases.android.common.screens

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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.accompanist.insets.ui.Scaffold
import com.turbosokol.mypurchases.android.common.components.AddPurchaseContent
import com.turbosokol.mypurchases.android.common.components.AppTopBar
import com.turbosokol.mypurchases.android.common.components.PurchaseColumnItem
import com.turbosokol.mypurchases.android.common.components.RightTopBarContentType
import com.turbosokol.mypurchases.android.core.ReduxViewModel
import com.turbosokol.mypurchases.common.app.AppState
import com.turbosokol.mypurchases.common.purchases.redux.PurchaseAction
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import org.koin.androidx.compose.getViewModel
import kotlin.time.ExperimentalTime

const val CATEGORIES_EXPANDED_VIEW_ROUTE = "Category Screen"

@ExperimentalComposeUiApi
@ExperimentalMaterial3Api
@ExperimentalMaterialApi
@ExperimentalTime
@Composable
fun CategoryExpandedScreen(
    viewModel: ReduxViewModel = getViewModel(),
    navController: NavController,
    onPurchaseClick: (Long) -> Unit
) {
    val stateFlow: StateFlow<AppState> = viewModel.store.observeAsState()
    val state by stateFlow.collectAsState(Dispatchers.Main)
    val categoriesState = state.getCategoriesState()
    val purchaseState = state.getPurchaseState()


    val scrollState = rememberLazyListState()
    val expandableList = categoriesState.expandableCategory
    viewModel.execute(PurchaseAction.GetAllPurchasesByParent(expandableList.title))
    val currentPurchasesList = purchaseState.purchaseItems

    val coroutineScope = rememberCoroutineScope()
    val bottomSheetState = rememberBottomSheetScaffoldState(
        bottomSheetState = BottomSheetState(BottomSheetValue.Collapsed)
    )

    BottomSheetScaffold(
        topBar = {
            AppTopBar(
                title = expandableList.title,
                onBackClick = { navController.popBackStack() },
                hasOptionsButton = false,
                onOptionsClick = { /*TODO*/ },
                hasRightButton = true,
                rightContentType = RightTopBarContentType.DELETE,
                onRightClick = { /*TODO*/ })

        },
        sheetContent = {
//            TODO("EditContent")
            AddPurchaseContent() },
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
                        coast = item.coast,
                        title = item.title,
                        onPurchaseClick = onPurchaseClick
                    )
                }

            }
        }
    }

}