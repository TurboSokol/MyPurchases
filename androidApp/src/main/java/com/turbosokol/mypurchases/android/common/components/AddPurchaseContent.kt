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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.turbosokol.mypurchases.android.R
import com.turbosokol.mypurchases.android.common.theme.AppTheme
import com.turbosokol.mypurchases.android.common.theme.MyPrimary
import com.turbosokol.mypurchases.android.common.utils.manageOrAddCategorySafety
import com.turbosokol.mypurchases.android.core.ReduxViewModel
import com.turbosokol.mypurchases.common.app.AppState
import com.turbosokol.mypurchases.common.navigation.redux.AppTopBarStateType
import com.turbosokol.mypurchases.common.navigation.redux.NavigationAction
import com.turbosokol.mypurchases.common.purchases.redux.PurchaseAction
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import org.koin.androidx.compose.getViewModel
import kotlin.time.ExperimentalTime
import androidx.compose.ui.res.stringResource as stringResource


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
    val purchasesState = state.getPurchaseState()
    val navigationState = state.getNavigationState()
    val allCategories = categoriesState.categoryItems
    val editablePurchase = purchasesState.editablePurchase
    val appTopBarStateType = navigationState.appTopBarStateType

    val categoryTitleValue =
        remember { mutableStateOf(if (appTopBarStateType == AppTopBarStateType.EDIT) editablePurchase.parent else "") }
    val coastValue =
        remember { mutableStateOf(if (appTopBarStateType == AppTopBarStateType.EDIT) editablePurchase.coast.toString() else "") }
    val descriptionValue =
        remember { mutableStateOf(if (appTopBarStateType == AppTopBarStateType.EDIT) editablePurchase.description.toString() else "") }
    val keyboard = LocalSoftwareKeyboardController.current
    val localContext = LocalContext.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp), elevation = 8.dp
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {

            // PARENT CATEGORY
            Row(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .padding(horizontal = 8.dp),
                verticalAlignment = Alignment.Bottom
            ) {


                TextFieldWithHint(
                    modifier = Modifier
                        .padding(start = 8.dp),
                    textFieldValue = categoryTitleValue.value,
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

            // COAST
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

            // DESCRIPTION OF PURCHASE
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
                    when (appTopBarStateType) {
                        AppTopBarStateType.DEFAULT -> {
                            if (coastValue.value.isEmpty()) {
                                Toast.makeText(
                                    localContext,
                                    R.string.empty_coast_toast,
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                            } else {
                                viewModel.execute(
                                    PurchaseAction.AddPurchase(
                                        parentTitle = categoryTitleValue.value,
                                        coast = coastValue.value.toDouble(),
                                        description = descriptionValue.value
                                    )
                                )
                                // Find categories with same title and update it or create new one
                                manageOrAddCategorySafety(
                                    allCategories = allCategories,
                                    categoryTitle = categoryTitleValue.value,
                                    spentSum = coastValue.value
                                )
                            }
                        }

                        AppTopBarStateType.EDIT -> {
                            if (coastValue.value.isEmpty()) {
                                Toast.makeText(
                                    localContext,
                                    "Please enter Coast",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                            } else {
                                viewModel.execute(
                                    PurchaseAction.EditPurchase(
                                        id = editablePurchase.id,
                                        parentTitle = categoryTitleValue.value,
                                        coast = coastValue.value.toDouble(),
                                        description = descriptionValue.value
                                    )
                                )
                                // Find categories with same title and update it or create new one
                                manageOrAddCategorySafety(
                                    allCategories = allCategories,
                                    categoryTitle = categoryTitleValue.value,
                                    spentSum = coastValue.value
                                )
                            }
                        }
                        else -> {}
                    }
                    categoryTitleValue.value = ""
                    coastValue.value = ""
                    descriptionValue.value = ""
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

