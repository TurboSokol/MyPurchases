package com.turbosokol.mypurchases.android.common.components

import android.icu.text.CaseMap
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun PurchaseColumnItem(coast: Long, title: String?, onPurchaseClick: (Long) -> Unit) {
    Card(modifier = Modifier.padding(8.dp), elevation = 16.dp) {
//        TODO()
        Row(modifier = Modifier.fillMaxWidth().padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
            Text(text = "Coast: ", style = MaterialTheme.typography.subtitle1)
            Text(modifier = Modifier.weight(0.3F), text = coast.toString())
            if (title != null) {
                Text(modifier = Modifier.weight(0.7F), text = title)
            }
        }
    }
}