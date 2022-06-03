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
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.turbosokol.mypurchases.android.R
import com.turbosokol.mypurchases.android.common.theme.AppTheme
import com.turbosokol.mypurchases.android.common.theme.MyRedColor
import com.turbosokol.mypurchases.android.core.ReduxViewModel
import com.turbosokol.mypurchases.common.app.AppState
import com.turbosokol.mypurchases.common.navigation.redux.AppTopBarStateType
import com.turbosokol.mypurchases.common.navigation.redux.NavigationAction
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import org.koin.androidx.compose.getViewModel
import kotlin.time.ExperimentalTime

@ExperimentalTime
@ExperimentalTransitionApi
@ExperimentalComposeUiApi
@Composable
fun PurchaseColumnItem(
    viewModel: ReduxViewModel = getViewModel(),
    coast: Double,
    description: String,
    keyboard: SoftwareKeyboardController?,
    appTopBarStateType: AppTopBarStateType,
    onPurchaseModified: (Double, String) -> Unit,
    onPurchaseDeleted: () -> Unit
) {

    val stateFlow: StateFlow<AppState> = viewModel.store.observeAsState()
    val state by stateFlow.collectAsState(Dispatchers.Main)
    val navigationState = state.getNavigationState()

    val checkChanges = navigationState.checkChanges

    val descriptionValue = remember { mutableStateOf(description) }
    val coastValue = remember { mutableStateOf(coast.toString()) }

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
            .clickable(enabled = (appTopBarStateType != AppTopBarStateType.DEFAULT)) {
                if (appTopBarStateType == AppTopBarStateType.DELETE) {
                    onPurchaseDeleted()
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
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // COAST
            Card(
                modifier = Modifier
                    .padding(horizontal = 4.dp)
                    .border(AppTheme.appBorderStroke)
                    .weight(0.4F),
                elevation = AppTheme.appLazyColumnItemElevation
            ) {
                Row {
                    //Coast title
                    Text(
                        modifier = Modifier
                            .padding(AppTheme.appPaddingMedium8)
                            .defaultMinSize(1.dp),
                        text = stringResource(R.string.purchase_column_item_coast_title),
                        style = MaterialTheme.typography.subtitle1
                    )
                    //Coast value
                    BasicTextField(modifier = Modifier
                        .padding(end = AppTheme.appPaddingNano1)
                        .border(
                            if (appTopBarStateType == AppTopBarStateType.EDIT) {
                                animatedBorder
                            } else {
                                BorderStroke((-1).dp, Color.Transparent)
                            }
                        )
                        .padding(AppTheme.appPaddingMedium8)

                        .align(CenterVertically),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(onDone = {
                            keyboard?.hide()
                            onPurchaseModified(coastValue.value.toDouble(), descriptionValue.value)
                        }), value = coastValue.value,
                        enabled = (appTopBarStateType == AppTopBarStateType.EDIT),
                        onValueChange = { text ->
                            val validateRegexPattern = """[0-9\\.]{0,64}""".toRegex()
                            coastValue.value = validateRegexPattern.find(text)?.value.toString()
                        })
                }
            }

            //Purchase description
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
                    .weight(0.6F),
                elevation = AppTheme.appLazyColumnItemElevation
            ) {
                BasicTextField(modifier = Modifier
                    .padding(AppTheme.appPaddingMedium8)
                    .defaultMinSize(1.dp)
                    .fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = {
                        keyboard?.hide()
                        onPurchaseModified(coastValue.value.toDouble(), descriptionValue.value)
                    }),
                    value = descriptionValue.value,
                    enabled = (appTopBarStateType == AppTopBarStateType.EDIT),
                    onValueChange = { descriptionValue.value = it })
            }
        }
    }

    if (checkChanges) {
        if (descriptionValue.value != description || coastValue.value != coast.toString()) {
            keyboard?.hide()
            onPurchaseModified(coastValue.value.toDouble(), descriptionValue.value)
        }
        viewModel.execute(NavigationAction.CheckChanges(false))
    }
}