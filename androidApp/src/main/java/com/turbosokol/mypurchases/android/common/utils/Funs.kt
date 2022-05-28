package com.turbosokol.mypurchases.android.common.utils

import com.turbosokol.mypurchases.android.core.ReduxViewModel
import com.turbosokol.mypurchases.common.categories.redux.CategoriesAction
import comturbosokolmypurchases.CategoriesDb
import org.koin.java.KoinJavaComponent.inject
import kotlin.time.ExperimentalTime

@ExperimentalTime
fun checkAndAddCategory(
                        allCategories: List<CategoriesDb>,
                        categoryTitleValue: String,
                        coastValue: String){

    val viewModel: ReduxViewModel by inject(ReduxViewModel::class.java)

    var targetCategory: CategoriesDb? = null
    allCategories.forEach { category ->
        if (category.title == categoryTitleValue) {
            targetCategory = category
        }
    }
    targetCategory?.let {
        //when user didn't specify expect sum in category
        if (it.expectedSum == it.spentSum) {
            viewModel.execute(
                CategoriesAction.AddCategories(
                    title = categoryTitleValue,
                    spentSum = (coastValue.toDouble() + it.spentSum),
                    expectedSum = (coastValue.toDouble() + (it.expectedSum))
                )
            )
        } else {
            //if user specify expect sum - expect sum holds
            viewModel.execute(
                CategoriesAction.AddCategories(
                    title = categoryTitleValue,
                    spentSum = (coastValue.toDouble() + it.spentSum),
                    expectedSum = it.expectedSum
                )
            )
        }
    } ?: run {
        //if category with added title doesn't create yet
        viewModel.execute(
            CategoriesAction.AddCategories(
                title = categoryTitleValue,
                spentSum = coastValue.toDouble(),
                expectedSum = coastValue.toDouble()
            )
        )
    }
}
