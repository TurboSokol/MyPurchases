package com.turbosokol.mypurchases.android.common.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Card
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.turbosokol.mypurchases.android.common.theme.AppTheme
import com.turbosokol.mypurchases.android.common.theme.MyDarkRedColor
import com.turbosokol.mypurchases.android.common.theme.MyPrimary
import com.turbosokol.mypurchases.android.common.theme.MyRedColor
import com.turbosokol.mypurchases.android.common.utils.editCategorySafety
import com.turbosokol.mypurchases.android.core.ReduxViewModel
import com.turbosokol.mypurchases.common.app.AppState
import com.turbosokol.mypurchases.common.categories.redux.CategoriesAction
import com.turbosokol.mypurchases.common.navigation.redux.AppTopBarStateType
import com.turbosokol.mypurchases.common.navigation.redux.NavigationAction
import com.turbosokol.mypurchases.common.purchases.redux.PurchaseAction
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import org.koin.androidx.compose.getViewModel
import kotlin.time.ExperimentalTime

@ExperimentalTime
@ExperimentalTransitionApi
@ExperimentalComposeUiApi
@Composable
fun CategoriesColumnItem(
    viewModel: ReduxViewModel = getViewModel(),
    id: Long,
    title: String,
    spentSum: Double,
    expectedSum: Double,
    keyboard: SoftwareKeyboardController?,
    appTopBarStateType: AppTopBarStateType,
    onCategoryManage: (title: String, spentSum: String, expectedSum: String) -> Unit,
    onCategoryClick: () -> Unit
) {
    val stateFlow: StateFlow<AppState> = viewModel.store.observeAsState()
    val state by stateFlow.collectAsState(Dispatchers.Main)
    val navigationState = state.getNavigationState()

    val checkChanges = navigationState.checkChanges

    var titleValue by remember {
        mutableStateOf("")
    }
    var spentSumValue by remember {
        mutableStateOf("")
    }

    var expectSumValue by remember {
        mutableStateOf("")
    }

    if (appTopBarStateType == AppTopBarStateType.DEFAULT) {
        titleValue = title
        spentSumValue = spentSum.toString()
        expectSumValue = expectedSum.toString()
    }


    val animationTransition = rememberInfiniteTransition()
    val animationElevation by animationTransition.animateValue(
        initialValue = 0.dp,
        targetValue = 24.dp,
        typeConverter = Dp.VectorConverter,
        animationSpec = InfiniteRepeatableSpec(
            tween(500),
            repeatMode = RepeatMode.Reverse
        )
    )
    val animationBorderWeight by animationTransition.animateValue(
        initialValue = 2.dp,
        targetValue = (-1).dp,
        typeConverter = Dp.VectorConverter,
        animationSpec = InfiniteRepeatableSpec(
            tween(500),
            repeatMode = RepeatMode.Reverse
        )
    )

    val animatedBorder = BorderStroke(animationBorderWeight, MyRedColor)


    Card(
        modifier = Modifier
            .padding(
                horizontal = AppTheme.appPaddingMedium8,
                vertical = AppTheme.appPaddingSmall3
            )
            .border(if (appTopBarStateType == AppTopBarStateType.DELETE) animatedBorder else AppTheme.appBorderStroke)
            .clickable {
                if (appTopBarStateType == AppTopBarStateType.DEFAULT) {
                    onCategoryClick()
                } else {
                    onCategoryManage(
                        titleValue,
                        spentSumValue,
                        expectSumValue
                    )
                }
            },
        elevation = if (appTopBarStateType == AppTopBarStateType.DEFAULT) {
            AppTheme.appLazyColumnItemElevation
        } else {
            animationElevation
        }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            //Title value
            Card(
                modifier = Modifier
                    .padding(horizontal = 4.dp, vertical = 4.dp)
                    .border(
                        if (appTopBarStateType == AppTopBarStateType.EDIT) {
                            animatedBorder
                        } else {
                            AppTheme.appBorderStroke
                        }
                    )
                    .weight(0.5F),
                elevation = AppTheme.appLazyColumnItemElevation
            ) {
                BasicTextField(modifier = Modifier
                    .padding(AppTheme.appPaddingMedium8)
                    .align(CenterVertically),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(onDone = {
                        keyboard?.hide()
                        onCategoryManage(titleValue, spentSumValue, expectSumValue)
                    }), value = titleValue,
                    enabled = (appTopBarStateType == AppTopBarStateType.EDIT),
                    onValueChange = { titleValue = it })
            }
            //Spent Sum
            Card(
                modifier = Modifier
                    .padding(horizontal = 4.dp)
                    .border(
                        if (appTopBarStateType == AppTopBarStateType.EDIT) {
                            animatedBorder
                        } else if (spentSum > expectedSum) {
                            BorderStroke(2.dp, MyDarkRedColor)
                        } else {
                            AppTheme.appBorderStroke
                        }
                    )
                    .weight(0.25F),
                elevation = AppTheme.appLazyColumnItemElevation
            ) {
                BasicTextField(modifier = Modifier
                    .padding(AppTheme.appPaddingMedium8)
                    .defaultMinSize(1.dp),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(onDone = {
                        keyboard?.hide()
                        onCategoryManage(titleValue, spentSumValue, expectSumValue)
                    }),
                    value = spentSumValue,
                    enabled = (appTopBarStateType == AppTopBarStateType.EDIT),
                    onValueChange = { text: String ->
                        val validateRegexPattern = """[0-9\\.]{0,64}""".toRegex()
                        spentSumValue = validateRegexPattern.find(text)?.value.toString()
                    }
                )
            }
            //Expect Sum
            Card(
                modifier = Modifier
                    .padding(horizontal = 4.dp)
                    .border(
                        if (appTopBarStateType == AppTopBarStateType.EDIT) {
                            animatedBorder
                        } else if (spentSum > expectedSum) {
                            BorderStroke(2.dp, MyPrimary)
                        } else {
                            AppTheme.appBorderStroke
                        }
                    )
                    .weight(0.25F),
                elevation = AppTheme.appLazyColumnItemElevation
            ) {
                BasicTextField(modifier = Modifier
                    .padding(AppTheme.appPaddingMedium8)
                    .defaultMinSize(1.dp),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(onDone = {
                        keyboard?.hide()
                        onCategoryManage(titleValue, spentSumValue, expectSumValue)
                    }),
                    value = expectSumValue,
                    enabled = (appTopBarStateType == AppTopBarStateType.EDIT),
                    onValueChange = { text ->
                        val validateRegexPattern = """[0-9\\.]{0,64}""".toRegex()
                        expectSumValue = validateRegexPattern.find(text)?.value.toString()
                    }
                )
            }
        }
    }

    if (checkChanges) {
        viewModel.execute(PurchaseAction.GetAllPurchasesByParent(title))
        viewModel.execute(NavigationAction.CheckChanges(false))
        if (titleValue != title || spentSumValue != spentSum.toString() || expectSumValue != expectedSum.toString()) {
            keyboard?.hide()
            val editablePurchasesList = state.getPurchaseState().purchaseItems
            editCategorySafety(
                categoryId = id,
                categoryTitle = titleValue,
                spentSum = spentSumValue,
                expectSum = expectSumValue,
                editablePurchaseItems = editablePurchasesList
            )
            viewModel.execute(
                CategoriesAction.EditCategories(
                    id,
                    titleValue,
                    spentSumValue.toDouble(),
                    expectSumValue.toDouble()
                )
            )
        }
    }
}





