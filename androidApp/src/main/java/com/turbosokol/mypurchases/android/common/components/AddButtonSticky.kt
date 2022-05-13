package com.turbosokol.mypurchases.android.common.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.turbosokol.mypurchases.android.R
import com.turbosokol.mypurchases.android.core.ReduxViewModel
import com.turbosokol.mypurchases.common.purchases.redux.PurchaseAction
import org.koin.androidx.compose.getViewModel
import kotlin.time.ExperimentalTime

@ExperimentalTime
@Composable
fun AddButtonSticky(viewModel: ReduxViewModel = getViewModel()) {
    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        Column(
            modifier = Modifier
                .padding(bottom = 8.dp)
                .background(Color.Transparent),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_add_circle),
                contentDescription = null,
                modifier = Modifier.clickable {
                    viewModel.execute(PurchaseAction.ShowingAddContent(true))
                })
        }
    }
}

