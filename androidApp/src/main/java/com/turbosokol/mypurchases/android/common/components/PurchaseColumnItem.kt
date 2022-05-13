package com.turbosokol.mypurchases.android.common.components

import android.icu.text.CaseMap
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun PurchaseColumnItem(coast: Long, title: String?) {
    Card() {
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(modifier = Modifier.weight(0.3F), text = coast.toString())
            if (title != null) {
                Text(modifier = Modifier.weight(0.7F), text = title)
            }
        }
    }
}