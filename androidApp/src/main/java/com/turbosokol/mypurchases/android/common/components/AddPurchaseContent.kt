package com.turbosokol.mypurchases.android.common.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.turbosokol.mypurchases.android.R
import com.turbosokol.mypurchases.android.core.ReduxViewModel
import com.turbosokol.mypurchases.common.app.AppState
import com.turbosokol.mypurchases.common.categories.redux.CategoriesAction
import com.turbosokol.mypurchases.common.navigation.redux.NavigationAction
import com.turbosokol.mypurchases.common.purchases.redux.PurchaseAction
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import org.koin.androidx.compose.getViewModel
import kotlin.time.ExperimentalTime


@ExperimentalTime
@Composable
fun AddPurchaseContent(viewModel: ReduxViewModel = getViewModel()) {

    val stateFlow: StateFlow<AppState> = viewModel.store.observeAsState()
    val state by stateFlow.collectAsState(Dispatchers.Main)
    val categoriesState = state.getCategoriesState()
    val allCategoriesInDb = categoriesState.categoryItems

    val coastValue = remember { mutableStateOf("") }
    val descriptionValue = remember { mutableStateOf("") }
    val listValue = remember { mutableStateOf("") }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp), elevation = 8.dp, border = BorderStroke(1.dp, Color.Black)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .padding(horizontal = 8.dp)
            ) {
                Text(
                    modifier = Modifier
                        .align(CenterVertically)
                        .weight(0.4F),
                    text = "List Title: ",
                    style = MaterialTheme.typography.body1,
                    textAlign = TextAlign.Center
                )
                TextField(
                    modifier = Modifier.padding(start = 8.dp),
                    value = listValue.value,
                    onValueChange = { listValue.value = it })
            }

            Row(
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .padding(top = 8.dp)
                    .align(CenterHorizontally)

            ) {
                Text(
                    modifier = Modifier
                        .align(CenterVertically)
                        .weight(0.4F),
                    text = "Coast: ",
                    style = MaterialTheme.typography.body1,
                    textAlign = TextAlign.Center
                )
                TextField(
                    modifier = Modifier.padding(start = 8.dp),
                    value = coastValue.value,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    onValueChange = {
                        coastValue.value = it
                    })
            }

            Row(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .padding(horizontal = 8.dp)
            ) {
                Text(
                    modifier = Modifier
                        .align(CenterVertically)
                        .weight(0.4F),
                    text = "Title: ",
                    style = MaterialTheme.typography.body1,
                    textAlign = TextAlign.Center
                )
                TextField(
                    modifier = Modifier.padding(start = 8.dp),
                    value = descriptionValue.value,
                    onValueChange = {
                        descriptionValue.value = it
                    })
            }

            Row(
                modifier = Modifier
                    .padding(vertical = 32.dp)
                    .align(CenterHorizontally)
            ) {
                Image(
                    modifier = Modifier.clickable {

                        if (coastValue.value.isNullOrEmpty()) {
                        } else {
                            viewModel.execute(
                                PurchaseAction.AddPurchase(
                                    purchaseId = 0,
                                    parentListId = 0,
                                    coast = coastValue.value.toLong(),
                                    description = descriptionValue.value
                                )
                            )
                            viewModel.execute(
                                CategoriesAction.AddCategories(
                                    title = listValue.value,
                                    spentSum = coastValue.value.toLong(),
                                    expectedSum = coastValue.value.toLong()
                                )
                            )
                            viewModel.execute(NavigationAction.HideAddContent())
                        }
                    },
                    painter = painterResource(id = R.drawable.ic_add),
                    contentDescription = null
                )
            }
        }
    }
}