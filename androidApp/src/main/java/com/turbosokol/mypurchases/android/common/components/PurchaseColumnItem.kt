package com.turbosokol.mypurchases.android.common.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.turbosokol.mypurchases.android.R
import com.turbosokol.mypurchases.android.common.theme.AppTheme
import com.turbosokol.mypurchases.common.navigation.redux.PurchasesStateType

@Composable
fun PurchaseColumnItem(coast: Double, title: String?, purchaseStateType: PurchasesStateType, onPurchaseClick: (Long) -> Unit) {
    val cardElevation = remember {
        mutableSetOf(AppTheme.appLazyColumnItemElevation)
    }
    Card(
        modifier = Modifier.padding(AppTheme.appPaddingMedium),
        elevation = AppTheme.appLazyColumnItemElevation,
        border = AppTheme.appBorderStroke
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 2.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Card(
                modifier = Modifier
                    .padding(horizontal = 4.dp)
                    .border(AppTheme.appBorderStroke)
                    .weight(0.4F),
                elevation = AppTheme.appLazyColumnItemElevation
            ) {
                Row() {
                    Text(modifier = Modifier.padding(AppTheme.appPaddingMedium),
                        text = stringResource(R.string.purchase_column_item_coast_title),
                        style = MaterialTheme.typography.subtitle1,
                    )
                    Text(modifier = Modifier.align(CenterVertically), text = coast.toString())
                }

            }

            if (title != null) {
                Card(
                    modifier = Modifier
                        .padding(horizontal = 4.dp)
                        .border(AppTheme.appBorderStroke)
                        .weight(0.7F),
                    elevation = AppTheme.appLazyColumnItemElevation
                ) {
                    Text(modifier = Modifier.padding(AppTheme.appPaddingMedium), text = title)
                }

            }
        }
    }
}