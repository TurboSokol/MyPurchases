package com.turbosokol.mypurchases.android.common.components

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import com.turbosokol.mypurchases.android.core.ReduxViewModel
import org.koin.androidx.compose.getViewModel
import kotlin.time.ExperimentalTime

@ExperimentalTime
@Composable
fun AddCategoryContent(viewModel: ReduxViewModel = getViewModel()) {
//    TODO("Add Category")
    
    Text(text = "Hui")
}