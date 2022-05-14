package com.turbosokol.mypurchases.android.common.components

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.turbosokol.mypurchases.android.R
import com.turbosokol.mypurchases.android.core.ReduxViewModel
import com.turbosokol.mypurchases.common.purchases.redux.PurchaseAction
import org.koin.androidx.compose.getViewModel
import kotlin.time.ExperimentalTime


@ExperimentalTime
@Composable
fun AddPurchaseContent(viewModel: ReduxViewModel = getViewModel()) {

    val coastValue = remember { mutableStateOf("") }
    val descriptionValue = remember { mutableStateOf("") }
    val listValue = remember { mutableStateOf("") }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp), elevation = 8.dp, border = BorderStroke(1.dp, Color.Black)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .padding(horizontal = 8.dp)
            ) {
                Text(
                    modifier = Modifier
                        .align(CenterVertically)
                        .weight(0.4F),
                    text = "List: ",
                    style = MaterialTheme.typography.body1,
                    textAlign = TextAlign.Center
                )
                TextField(
                    modifier = Modifier.padding(start = 8.dp),
                    value = listValue.value,
                    onValueChange = { listValue.value = it })
            }

            Row(
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .padding(top = 8.dp)
                    .align(CenterHorizontally)

            ) {
                Text(
                    modifier = Modifier
                        .align(CenterVertically)
                        .weight(0.4F),
                    text = "Coast: ",
                    style = MaterialTheme.typography.body1,
                    textAlign = TextAlign.Center
                )
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
                    .padding(horizontal = 8.dp)
            ) {
                Text(
                    modifier = Modifier
                        .align(CenterVertically)
                        .weight(0.4F),
                    text = "Title: ",
                    style = MaterialTheme.typography.body1,
                    textAlign = TextAlign.Center
                )
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
                            TODO("CREATE LIST by Action")
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