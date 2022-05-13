package com.turbosokol.mypurchases.android.common.components

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.turbosokol.mypurchases.android.R
import com.turbosokol.mypurchases.android.core.ReduxViewModel
import com.turbosokol.mypurchases.android.core.Service
import com.turbosokol.mypurchases.common.purchases.redux.PurchaseAction
import org.koin.androidx.compose.getViewModel
import kotlin.time.ExperimentalTime

@ExperimentalMaterialApi
@ExperimentalTime
@Composable
fun AddPurchaseBottomSheet() {

    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = BottomSheetState(BottomSheetValue.Collapsed)
    )

    BottomSheetScaffold(
        scaffoldState = bottomSheetScaffoldState,
        sheetContent = { AddPurchaseContent() },
        sheetElevation = 2.dp,
        sheetPeekHeight = 16.dp
    ) {

    }


}

@ExperimentalTime
@Composable
fun AddPurchaseContent(viewModel: ReduxViewModel = getViewModel()) {

    val coastValue = remember { mutableStateOf("") }
    val descriptionValue = remember { mutableStateOf("") }
    val listValue = remember { mutableStateOf("") }

    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.fillMaxHeight()) {
            Row(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .padding(horizontal = 32.dp)
            ) {
                Text(text = "List: ")
                TextField(
                    modifier = Modifier.padding(start = 8.dp),
                    value = listValue.value,
                    onValueChange = { listValue.value = it })
            }

            Row(
                modifier = Modifier
                    .padding(horizontal = 32.dp)
                    .padding(top = 8.dp)
            ) {
                Text(text = "Coast: ")
                TextField(
                    modifier = Modifier.padding(start = 8.dp),
                    value = coastValue.value,
                    onValueChange = {
                        coastValue.value = it
                    })
            }

            Row(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .padding(horizontal = 32.dp)
            ) {
                Text(text = "Description: ")
                TextField(
                    modifier = Modifier.padding(start = 8.dp),
                    value = descriptionValue.value,
                    onValueChange = {
                        descriptionValue.value = it
                    })
            }

            Row(
                modifier = Modifier
                    .padding(vertical = 16.dp)
                    .align(CenterHorizontally)
            ) {
                Image(
                    modifier = Modifier.clickable {
                        if (coastValue.value.isNullOrEmpty()) {

                        } else {
                            viewModel.execute(
                                PurchaseAction.AddPurchase(
                                    purchaseId = 0,
                                    parentListId = 0,
                                    coast = coastValue.value.toLong(),
                                    description = descriptionValue.value
                                )
                            )
                            viewModel.execute(PurchaseAction.ShowingAddContent(false))
                        }
                    },
                    painter = painterResource(id = R.drawable.ic_add_circle),
                    contentDescription = null
                )
            }
        }
    }
}