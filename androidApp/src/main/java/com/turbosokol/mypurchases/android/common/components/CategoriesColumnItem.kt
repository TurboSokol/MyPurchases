package com.turbosokol.mypurchases.android.common.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CategoriesColumnItem(title: String, spentSum: Long, expectedSum: Long, onItemClick:(String) -> Unit) {
    Card(modifier =Modifier.clickable {
        onItemClick(title)
    }) {
        Row(modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)) {
            Text(modifier = Modifier.weight(0.5F), text = title)
            Text(modifier = Modifier.weight(0.25F), text = spentSum.toString())
            Text(modifier = Modifier.weight(0.25F), text = expectedSum.toString())
        }
    }
}