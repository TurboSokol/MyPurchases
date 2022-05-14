package com.turbosokol.mypurchases.android.common.pages

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.google.accompanist.insets.ui.Scaffold
import com.turbosokol.mypurchases.android.common.components.PurchaseColumnItem
import com.turbosokol.mypurchases.android.core.ReduxViewModel
import com.turbosokol.mypurchases.common.app.AppState
import com.turbosokol.mypurchases.common.lists.redux.ListsAction
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import org.koin.androidx.compose.getViewModel
import kotlin.time.ExperimentalTime

const val LIST_EXPANDED_VIEW_ROUTE = "List expanded view route"
const val LIST_ID = "list id"

@ExperimentalTime
@Composable
fun ListExpandedPage(
    viewModel: ReduxViewModel = getViewModel(),
    navController: NavController,
    listId: Long,
    onItemClick: () -> Unit
) {
    val stateFlow: StateFlow<AppState> = viewModel.store.observeAsState()
    val state by stateFlow.collectAsState(Dispatchers.Main)
    val listsState = state.getListsState()
    val purchaseState = state.getPurchaseState()

    viewModel.execute(ListsAction.GetList(listId))

    val scrollState = rememberLazyListState()
    val expandableList = listsState.expandableList
    val currentPurchasesList = purchaseState.purchaseItems

    Scaffold() {
        Column(modifier = Modifier.fillMaxSize()) {
            Row(verticalAlignment = Alignment.Top) {
                Text(text = "")
            }
            LazyColumn(state = scrollState) {
                itemsIndexed(currentPurchasesList) { index, item ->
                    PurchaseColumnItem(coast = item.coast, title = item.title)
                }

            }
        }
    }

}