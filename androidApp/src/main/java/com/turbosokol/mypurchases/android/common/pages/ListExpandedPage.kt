package com.turbosokol.mypurchases.android.common.pages

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import com.google.accompanist.insets.ui.Scaffold
import com.turbosokol.mypurchases.android.core.ReduxViewModel
import org.koin.androidx.compose.getViewModel
import kotlin.time.ExperimentalTime

const val LIST_EXPANDED_VIEW_ROUTE = "List expanded view route"

@ExperimentalTime
@Composable
fun ListExpandedPage(viewModel: ReduxViewModel = getViewModel()) {
    Scaffold() {
        Text(text = "EXPANDED VIEW HERE")
    }
    
}