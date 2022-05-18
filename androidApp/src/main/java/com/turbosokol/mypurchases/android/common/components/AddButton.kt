package com.turbosokol.mypurchases.android.common.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.turbosokol.mypurchases.android.R
import com.turbosokol.mypurchases.android.common.theme.AppTheme
import com.turbosokol.mypurchases.android.common.theme.MyPrimary
import com.turbosokol.mypurchases.android.core.ReduxViewModel
import com.turbosokol.mypurchases.common.navigation.redux.AddButtonContentType
import com.turbosokol.mypurchases.common.navigation.redux.NavigationAction
import org.koin.androidx.compose.getViewModel
import kotlin.time.ExperimentalTime

@ExperimentalTime
@Composable
fun AddButton(
    modifier: Modifier = Modifier,
    viewModel: ReduxViewModel = getViewModel(),
    contentType: AddButtonContentType
) {
    Box(modifier = modifier) {
        Column(
            modifier = Modifier
                .padding(bottom = 8.dp)
                .background(Color.Transparent),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val title = when (contentType) {
                AddButtonContentType.PURCHASE -> {
                    stringResource(R.string.new_purchase_button)
                }
                AddButtonContentType.CATEGORY -> {
                    stringResource(R.string.new_category_button)
                }
                else -> {
                    ""
                }
            }
            Button(modifier = Modifier.padding(16.dp),
                elevation = ButtonDefaults.elevatedButtonElevation(defaultElevation = AppTheme.appButtonElevation),
                colors = ButtonDefaults.buttonColors(MyPrimary),
                border = AppTheme.appBorderStroke,
                onClick = {
                    viewModel.execute(NavigationAction.ShowAddContent(true, contentType))
                }) {
                Text(text = title, style = MaterialTheme.typography.subtitle2)
            }
        }
    }
}



