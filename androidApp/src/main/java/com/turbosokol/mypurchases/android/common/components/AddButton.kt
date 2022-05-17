package com.turbosokol.mypurchases.android.common.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.turbosokol.mypurchases.android.R
import com.turbosokol.mypurchases.android.common.theme.MyPrimary
import com.turbosokol.mypurchases.android.core.ReduxViewModel
import com.turbosokol.mypurchases.common.purchases.redux.PurchaseAction
import org.koin.androidx.compose.getViewModel
import kotlin.time.ExperimentalTime

@ExperimentalTime
@Composable
fun AddButton(viewModel: ReduxViewModel = getViewModel(),
contentType: AddButtonContentType) {
    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        Column(
            modifier = Modifier
                .padding(bottom = 8.dp)
                .background(Color.Transparent),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val title = when (contentType) {
                AddButtonContentType.PURCHASE -> {
                    "NEW PURCHASE"
                }
                AddButtonContentType.CATEGORY -> {
                    "NEW CATEGORY"
                }
                else -> {""}
            }
            Button(modifier = Modifier.padding(16.dp),
                elevation = ButtonDefaults.buttonElevation(),
                colors = ButtonDefaults.buttonColors(MyPrimary),
                onClick = {
                    viewModel.execute(PurchaseAction.ShowingAddContent(true, "PURCHASE"))
                }) {
                (
                        Text(text = title)
                )
            }
        }
    }
}

enum class AddButtonContentType {
    PURCHASE,
    CATEGORY
}

