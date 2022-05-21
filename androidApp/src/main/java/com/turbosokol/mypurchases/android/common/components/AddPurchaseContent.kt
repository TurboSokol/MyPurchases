package com.turbosokol.mypurchases.android.common.components

import android.widget.Toast
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
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
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
import comturbosokolmypurchases.CategoriesDb
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import org.koin.androidx.compose.getViewModel
import kotlin.time.ExperimentalTime


@ExperimentalMaterial3Api
@ExperimentalMaterialApi
@ExperimentalComposeUiApi
@ExperimentalTime
@Composable
fun AddPurchaseContent(
    viewModel: ReduxViewModel = getViewModel(),
    keyboard: SoftwareKeyboardController?
) {

    val stateFlow: StateFlow<AppState> = viewModel.store.observeAsState()
    val state by stateFlow.collectAsState(Dispatchers.Main)
    val categoriesState = state.getCategoriesState()
    val allCategories = categoriesState.categoryItems

    val categoryTitleValue = remember { mutableStateOf("") }
    val coastValue = remember { mutableStateOf("") }
    val descriptionValue = remember { mutableStateOf("") }
    val keyboard = LocalSoftwareKeyboardController.current
    val localContext = LocalContext.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp), elevation = 8.dp
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
                    titleText = stringResource(R.string.add_category_text),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    onHintUsed = {
                        keyboard?.hide()
                    })
                {
                    categoryTitleValue.value = it
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
                    text = stringResource(R.string.add_coast_text),
                    style = MaterialTheme.typography.body1,
                    textAlign = TextAlign.Center
                )
                TextField(
                    modifier = Modifier.padding(start = 8.dp),
                    value = coastValue.value,
                    onValueChange = { text ->
                        val validateRegexPattern = """[0-9\\.]{0,64}""".toRegex()
                        coastValue.value = validateRegexPattern.find(text)?.value.toString()
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
                    text = stringResource(R.string.add_title_text),
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
                    .padding(vertical = 12.dp)
                    .align(CenterHorizontally),
                border = AppTheme.appBorderStroke,
                elevation = ButtonDefaults.buttonElevation(AppTheme.appButtonElevation),
                colors = ButtonDefaults.buttonColors(MyPrimary),
                onClick = {
                    if (coastValue.value.isNullOrEmpty()) {
                        Toast.makeText(localContext, "Please enter Coast", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        viewModel.execute(
                            PurchaseAction.AddPurchase(
                                parentTitle = categoryTitleValue.value,
                                coast = coastValue.value.toDouble(),
                                description = descriptionValue.value
                            )
                        )

                        var targetCategory: CategoriesDb? = null
                        allCategories.forEach { category ->
                            if (category.title == categoryTitleValue.value) {
                                targetCategory = category
                            }
                        }
                        targetCategory?.let {
                            //when user didn't specify expect sum in category
                            if (it.expectedSum == it.spentSum) {
                                viewModel.execute(
                                    CategoriesAction.AddCategories(
                                        title = categoryTitleValue.value,
                                        spentSum = (coastValue.value.toDouble() + it.spentSum),
                                        expectedSum = (coastValue.value.toDouble() + (it.expectedSum))
                                    )
                                )
                            } else {
                                //if user specify expect sum - expect sum holds
                                viewModel.execute(
                                    CategoriesAction.AddCategories(
                                        title = categoryTitleValue.value,
                                        spentSum = (coastValue.value.toDouble() + it.spentSum),
                                        expectedSum = it.expectedSum
                                    )
                                )
                            }
                        }
                    } ?: run {
                        //if category with added title doesn't create yet
                        viewModel.execute(
                            CategoriesAction.AddCategories(
                                title = categoryTitleValue.value,
                                spentSum = coastValue.value.toDouble(),
                                expectedSum = coastValue.value.toDouble()
                            )
                        )
                    }

                    viewModel.execute(NavigationAction.HideAddContent())
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

