package com.turbosokol.mypurchases.android.common.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.turbosokol.mypurchases.android.R
import com.turbosokol.mypurchases.android.common.theme.AppTheme
import com.turbosokol.mypurchases.android.common.theme.MyPrimary
import com.turbosokol.mypurchases.android.core.ReduxViewModel
import com.turbosokol.mypurchases.common.app.AppState
import com.turbosokol.mypurchases.common.categories.redux.CategoriesAction
import com.turbosokol.mypurchases.common.navigation.redux.NavigationAction
import com.turbosokol.mypurchases.common.purchases.redux.PurchaseAction
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import org.koin.androidx.compose.getViewModel
import kotlin.time.ExperimentalTime


@ExperimentalMaterial3Api
@ExperimentalMaterialApi
@ExperimentalComposeUiApi
@ExperimentalTime
@Composable
fun AddPurchaseContent(viewModel: ReduxViewModel = getViewModel()) {

    val stateFlow: StateFlow<AppState> = viewModel.store.observeAsState()
    val state by stateFlow.collectAsState(Dispatchers.Main)
    val categoriesState = state.getCategoriesState()
    val allCategories = categoriesState.categoryItems

    val coastValue = remember { mutableStateOf("") }
    val descriptionValue = remember { mutableStateOf("") }
    val listTitleValue = remember { mutableStateOf("") }
    val keyboard = LocalSoftwareKeyboardController.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp), elevation = 8.dp
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .padding(horizontal = 8.dp),
                verticalAlignment = Alignment.Bottom
            ) {


                TextFieldWithHint(
                    modifier = Modifier
                        .padding(start = 8.dp),
                    hintList = allCategories,
                    hasTitle = true,
                    titleText = stringResource(R.string.add_sheet_lists_title),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    onHintUsed = {
                        keyboard?.hide()
                    })
                {
                    listTitleValue.value = it
                }
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
                androidx.compose.material3.TextField(
                    modifier = Modifier.padding(start = 8.dp),
                    value = coastValue.value,
                    onValueChange = {
                        coastValue.value = it
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    )
                )
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
                    },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = { keyboard?.hide() })
                )
            }

                Button(
                    modifier = Modifier
                        .padding(vertical = 32.dp)
                        .align(CenterHorizontally),
                    border = AppTheme.appBorderStroke,
                    elevation = ButtonDefaults.buttonElevation(AppTheme.appButtonElevation),
                    colors = ButtonDefaults.buttonColors(MyPrimary),
                    onClick = {
                        if (coastValue.value.isNullOrEmpty()) {
//                            TODO("User warning")
                        } else {
                            viewModel.execute(
                                PurchaseAction.AddPurchase(
                                    parentTitle = listTitleValue.value,
                                    coast = coastValue.value.toLong(),
                                    description = descriptionValue.value
                                )
                            )
                            viewModel.execute(
                                CategoriesAction.AddCategories(
                                    title = listTitleValue.value,
                                    spentSum = coastValue.value.toLong(),
                                    expectedSum = coastValue.value.toLong()
                                )
                            )
                            viewModel.execute(NavigationAction.HideAddContent())
                        }
                    }
                ) {
                    Image(
                        modifier = Modifier,
                        painter = painterResource(id = R.drawable.ic_add),
                        contentDescription = null
                    )
                }
            }
        }
    }
