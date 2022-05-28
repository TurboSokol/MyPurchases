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
import com.turbosokol.mypurchases.common.navigation.redux.PurchasesStateType
import com.turbosokol.mypurchases.common.purchases.redux.PurchaseState

@ExperimentalTransitionApi
@ExperimentalComposeUiApi
@Composable
fun PurchaseColumnItem(
    coast: Double,
    description: String,
    purchaseStateType: PurchasesStateType,
    keyboard: SoftwareKeyboardController?,
    onPurchaseModified: (Double, String) -> Unit,
    onPurchaseDeleted: () -> Unit
) {

    val cardBorder = remember { mutableStateOf(AppTheme.appBorderStroke) }
    val cardAlpha = remember { mutableStateOf(1F) }
    val descriptionValue = remember { mutableStateOf(description) }
    val coastValue = remember { mutableStateOf(coast.toString()) }


    val animationTransition = rememberInfiniteTransition()
    val animatedAlpha by animationTransition.animateFloat(
        initialValue = 0.2F,
        targetValue = 0.9F,
        animationSpec = InfiniteRepeatableSpec(
            tween(500),
            repeatMode = RepeatMode.Reverse
        )
    )
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

//    var editAnimationState by remember { mutableStateOf(AnimationState.InitialState) }
//    val animationTransition = updateTransition(targetState = editAnimationState, label = "")
//    val firstAnimationTransition =
//        animationTransition.createChildTransition { it != AnimationState.InitialState }
//    val secondAnimationTransition =
//        animationTransition.createChildTransition { it == AnimationState.SecondState }
//    val editFirstAlfa by firstAnimationTransition.animateFloat(
//        transitionSpec = { tween(durationMillis = 2000) },
//        label = ""
//    ) {
//        if (it) 0.8F else 0.0F
//    }
//    val editFirstElevation by firstAnimationTransition.animateDp(
//        transitionSpec = { tween(durationMillis = 2000) },
//        label = ""
//    ) {
//        if (it) 30.dp else 10.dp
//    }
//    val editSecondAlfa by secondAnimationTransition.animateFloat(transitionSpec = {
//        tween(
//            durationMillis = 500
//        )
//    }, label = "") {
//        if (it) 1F else 0.2F
//    }


    if (purchaseStateType == PurchasesStateType.DEFAULT) {
        cardBorder.value = AppTheme.appBorderStroke
        cardAlpha.value = 1F
    } else {
        cardBorder.value = BorderStroke(animationBorderWeight, Color.Red)
        cardAlpha.value = animatedAlpha
    }


    Card(
        modifier = Modifier
            .padding(
                horizontal = AppTheme.appPaddingMedium,
                vertical = AppTheme.appPaddingSmall
            )
            .clickable(enabled = (purchaseStateType != PurchasesStateType.DEFAULT)) {
                if (purchaseStateType == PurchasesStateType.DELETE) {
                    onPurchaseDeleted()
                }
            },
        elevation = if (purchaseStateType == PurchasesStateType.DEFAULT) {
            AppTheme.appLazyColumnItemElevation
        } else {
            animationElevation
        }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 2.dp, vertical = 4.dp),
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
                Row() {
                    //Coast title
                    Text(
                        modifier = Modifier
                            .padding(AppTheme.appPaddingMedium)
                            .defaultMinSize(1.dp),
                        text = stringResource(R.string.purchase_column_item_coast_title),
                        style = MaterialTheme.typography.subtitle1
                    )
                    //Coast value
                    BasicTextField(modifier = Modifier
                        .padding(end = AppTheme.appPaddingNano)
                        .border(
                            if (purchaseStateType == PurchasesStateType.EDIT) cardBorder.value else BorderStroke(
                                0.dp,
                                Color.Transparent
                            )
                        )
                        .padding(AppTheme.appPaddingMedium)

                        .align(CenterVertically),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(onDone = {
                            keyboard?.hide()
                            onPurchaseModified(coastValue.value.toDouble(), descriptionValue.value)
                        }), value = coastValue.value,
                        enabled = (purchaseStateType == PurchasesStateType.EDIT),
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
                    .border(cardBorder.value)
                    .weight(0.7F),
                elevation = AppTheme.appLazyColumnItemElevation
            ) {
                BasicTextField(modifier = Modifier
                    .padding(AppTheme.appPaddingMedium)
                    .defaultMinSize(1.dp)
                    .fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = {
                        keyboard?.hide()
                        onPurchaseModified(coastValue.value.toDouble(), descriptionValue.value)
                    }),
                    value = descriptionValue.value,
                    enabled = (purchaseStateType == PurchasesStateType.EDIT),
                    onValueChange = { descriptionValue.value = it })
            }
        }
    }

    if (purchaseStateType != PurchasesStateType.EDIT) {
        keyboard?.hide()
        if (descriptionValue.value != description || coastValue.value != coast.toString()) {
            onPurchaseModified(coastValue.value.toDouble(), descriptionValue.value)
        }
    }

}
