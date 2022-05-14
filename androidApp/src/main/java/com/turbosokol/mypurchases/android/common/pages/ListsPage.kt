package com.turbosokol.mypurchases.android.common.pages

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Bottom
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.accompanist.insets.ui.Scaffold
import com.turbosokol.mypurchases.android.common.components.*
import com.turbosokol.mypurchases.android.core.ReduxViewModel
import com.turbosokol.mypurchases.common.app.AppState
import com.turbosokol.mypurchases.common.lists.redux.ListsAction
import com.turbosokol.mypurchases.common.purchases.redux.PurchaseAction
import io.ktor.http.cio.websocket.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getViewModel
import kotlin.time.ExperimentalTime

const val LIST_PAGE_ROUTE = "lists page route"

@SuppressLint("CoroutineCreationDuringComposition")
@ExperimentalMaterialApi
@ExperimentalTime
@Composable
fun ListsPage(
    viewModel: ReduxViewModel = getViewModel(),
    navController: NavController,
    onItemClick: (Long) -> Unit
) {
    val stateFlow: StateFlow<AppState> = viewModel.store.observeAsState()
    val state by stateFlow.collectAsState(Dispatchers.Main)
    val listsState = state.getListsState()
    val purchaseState = state.getPurchaseState()

    viewModel.execute(ListsAction.GetAllLists)

    val scrollState = rememberLazyListState()
    val listItems = listsState.listItems

    val coroutineScope = rememberCoroutineScope()
    val showAddSContent =  purchaseState.showAddContent
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
                        Text("LISTS ARE EMPTY")
                    } else {
                        LazyColumn(state = scrollState) {
                            itemsIndexed(listItems) { index, item ->
                                ListColumnItem(
                                    title = item.title,
                                    spentSum = item.spentSum,
                                    expectedSum = item.expectedSum
                                ) {
                                    onItemClick(item.id)
                                }
                            }
                        }
                    }
                }

                Row(verticalAlignment = Bottom) {
//                    if (!showAddSContent) {
                        AddButtonSticky()
//                    }
                }
            }
        }
    }
}
