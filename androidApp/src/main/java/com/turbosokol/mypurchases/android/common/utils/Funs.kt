package com.turbosokol.mypurchases.android.common.utils

import com.turbosokol.mypurchases.android.core.ReduxViewModel
import com.turbosokol.mypurchases.common.categories.redux.CategoriesAction
import com.turbosokol.mypurchases.common.purchases.redux.PurchaseAction
import comturbosokolmypurchases.CategoriesDb
import comturbosokolmypurchases.PurchaseDb
import org.koin.java.KoinJavaComponent.inject
import kotlin.time.ExperimentalTime

@ExperimentalTime
fun manageOrAddCategory(
    allCategories: List<CategoriesDb>,
    categoryTitle: String,
    spentSum: String
) {
    val viewModel: ReduxViewModel by inject(ReduxViewModel::class.java)

    var editableCategory: CategoriesDb? = null
    allCategories.forEach { category ->
        if (category.title == categoryTitle) {
            editableCategory = category
        }
    }

    editableCategory?.let { editableCategory ->
        //when user didn't specify expect sum in category
        if (editableCategory.expectedSum == editableCategory.spentSum) {
            viewModel.execute(
                CategoriesAction.EditCategories(
                    id = editableCategory.id,
                    title = categoryTitle,
                    spentSum = (spentSum.toDouble() + editableCategory.spentSum),
                    expectedSum = (spentSum.toDouble() + editableCategory.expectedSum)
                )
            )
        } else {
            //if user specify expect sum - expect sum holds
            viewModel.execute(
                CategoriesAction.EditCategories(
                    id = editableCategory.id,
                    title = categoryTitle,
                    spentSum = (spentSum.toDouble() + editableCategory.spentSum),
                    expectedSum = (editableCategory.expectedSum)
                )
            )
        }
    } ?: run {
        //if category with added title doesn't create yet
        viewModel.execute(
            CategoriesAction.InsertCategories(
                title = categoryTitle,
                spentSum = spentSum.toDouble(),
                expectedSum = spentSum.toDouble()
            )
        )
    }
}

@ExperimentalTime
fun editCategory(
    categoryId: Long,
    categoryTitle: String,
    spentSum: String,
    expectSum: String,
    editablePurchaseItems: List<PurchaseDb>
) {
    val viewModel: ReduxViewModel by inject(ReduxViewModel::class.java)

    viewModel.execute(
        CategoriesAction.EditCategories(
            categoryId,
            categoryTitle,
            spentSum.toDouble(),
            expectSum.toDouble()
        )
    )

    editablePurchaseItems.forEach {
        viewModel.execute(PurchaseAction.EditPurchase(it.id, categoryTitle, it.coast, it.description))
    }

}


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
