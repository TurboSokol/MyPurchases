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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import org.koin.androidx.compose.getViewModel
import kotlin.time.ExperimentalTime

@ExperimentalMaterial3Api
@ExperimentalMaterialApi
@ExperimentalComposeUiApi
@ExperimentalTime
@Composable
fun AddCategoryContent(viewModel: ReduxViewModel = getViewModel()) {

    val stateFlow: StateFlow<AppState> = viewModel.store.observeAsState()
    val state by stateFlow.collectAsState(Dispatchers.Main)
    val categoriesState = state.getCategoriesState()
    val allCategories = categoriesState.categoryItems

    val titleValue = remember { mutableStateOf("") }
    val expectSumValue = remember { mutableStateOf("") }

    val keyboard = LocalSoftwareKeyboardController.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp), elevation = 8.dp
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {

            Row(
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .padding(top = 8.dp)
                    .align(Alignment.CenterHorizontally)

            ) {
                Text(
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .weight(0.4F),
                    text = stringResource(id = R.string.add_title_text),
                    style = MaterialTheme.typography.body1,
                    textAlign = TextAlign.Center
                )
                TextField(
                    modifier = Modifier.padding(start = 8.dp),
                    value = titleValue.value,
                    onValueChange = {
                        titleValue.value = it
                    },
                    keyboardOptions = KeyboardOptions(
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
                        .align(Alignment.CenterVertically)
                        .weight(0.4F),
                    text = stringResource(R.string.add_excpect_sum_text),
                    style = MaterialTheme.typography.body1,
                    textAlign = TextAlign.Center
                )
                TextField(
                    modifier = Modifier.padding(start = 8.dp),
                    value = expectSumValue.value,
                    onValueChange = {
                        expectSumValue.value = it
                    },
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Done,
                        keyboardType = KeyboardType.Number
                    ),
                    keyboardActions = KeyboardActions(onDone = { keyboard?.hide() })
                )
            }

            Button(
                modifier = Modifier
                    .padding(vertical = 32.dp)
                    .align(Alignment.CenterHorizontally),
                border = AppTheme.appBorderStroke,
                elevation = ButtonDefaults.buttonElevation(AppTheme.appButtonElevation),
                colors = ButtonDefaults.buttonColors(MyPrimary),
                onClick = {
                    if (titleValue.value.isNullOrEmpty()) {
//                            TODO("User warning")
                    } else {
                        viewModel.execute(
                            CategoriesAction.AddCategories(
                                title = titleValue.value,
                                spentSum = 0L,
                                expectedSum = expectSumValue.value.toLong()
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
