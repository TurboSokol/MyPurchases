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
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.turbosokol.mypurchases.android.common.theme.AppTheme
import com.turbosokol.mypurchases.android.common.theme.MyRedColor
import com.turbosokol.mypurchases.android.common.utils.recalculateCategory
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

    val titleValue = remember { mutableStateOf(title) }
    val spentSumValue = remember { mutableStateOf(spentSum.toString()) }
    val expectSumValue = remember { mutableStateOf(expectedSum.toString()) }

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
                        titleValue.value,
                        spentSumValue.value,
                        expectSumValue.value
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
                        onCategoryManage(titleValue.value, spentSumValue.value, expectSumValue.value)
                    }), value = titleValue.value,
                    enabled = (appTopBarStateType == AppTopBarStateType.EDIT),
                    onValueChange = { titleValue.value = it })
            }
            //Spent Sum
            Card(
                modifier = Modifier
                    .padding(horizontal = 4.dp)
                    .border(
                        if (appTopBarStateType == AppTopBarStateType.EDIT) {
                            animatedBorder
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
                        onCategoryManage(titleValue.value, spentSumValue.value, expectSumValue.value)
                    }),
                    value = spentSumValue.value,
                    enabled = (appTopBarStateType == AppTopBarStateType.EDIT),
                    onValueChange = { text ->
                        val validateRegexPattern = """[0-9\\.]{0,64}""".toRegex()
                        spentSumValue.value = validateRegexPattern.find(text)?.value.toString()
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
                        onCategoryManage(titleValue.value, spentSumValue.value, expectSumValue.value)
                    }),
                    value = expectSumValue.value,
                    enabled = (appTopBarStateType == AppTopBarStateType.EDIT),
                    onValueChange = { text ->
                        val validateRegexPattern = """[0-9\\.]{0,64}""".toRegex()
                        expectSumValue.value = validateRegexPattern.find(text)?.value.toString()
                    }
                )
            }
        }
    }

    if (checkChanges) {
        viewModel.execute(PurchaseAction.GetAllPurchasesByParent(title))
        viewModel.execute(NavigationAction.CheckChanges(false))
        if (titleValue.value != title || spentSumValue.value != spentSum.toString() || expectSumValue.value != expectedSum.toString()) {
            keyboard?.hide()
            val editablePurchasesList = state.getPurchaseState().purchaseItems
            recalculateCategory(categoryId = id, categoryTitle = titleValue.value, spentSum = spentSumValue.value, expectSum = expectSumValue.value, editablePurchaseItems = editablePurchasesList)
            viewModel.execute(CategoriesAction.EditCategories(id, titleValue.value, spentSumValue.value.toDouble(), expectSumValue.value.toDouble()))
        }
    }
}





