package com.turbosokol.mypurchases.android.common.screens

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
import com.turbosokol.mypurchases.common.categories.redux.CategoriesAction
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import org.koin.androidx.compose.getViewModel
import kotlin.time.ExperimentalTime

const val CATEGORIES_EXPANDED_VIEW_ROUTE = "Category Screen"
const val CATEGORY_TITLE = "Category title"

@ExperimentalTime
@Composable
fun CategoryExpandedScreen(
    viewModel: ReduxViewModel = getViewModel(),
    navController: NavController,
    categoriesTitle: String,
    onPurchaseClick: (Long) -> Unit
) {
    val stateFlow: StateFlow<AppState> = viewModel.store.observeAsState()
    val state by stateFlow.collectAsState(Dispatchers.Main)
    val categoriesState = state.getCategoriesState()
    val purchaseState = state.getPurchaseState()

    viewModel.execute(CategoriesAction.GetCategory(categoriesTitle))

    val scrollState = rememberLazyListState()
    val expandableList = categoriesState.expandableCategory
    val currentPurchasesList = purchaseState.purchaseItems

    Scaffold() {
        Column(modifier = Modifier.fillMaxSize()) {
            Row(verticalAlignment = Alignment.Top) {
                Text(text = "")
            }
            LazyColumn(state = scrollState) {
                itemsIndexed(currentPurchasesList) { index, item ->
                    PurchaseColumnItem(coast = item.coast, title = item.title, onPurchaseClick = onPurchaseClick)
                }

            }
        }
    }

}