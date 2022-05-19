package com.turbosokol.mypurchases.android.common.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.turbosokol.mypurchases.android.R
import comturbosokolmypurchases.CategoriesDb

@ExperimentalMaterialApi
@ExperimentalMaterial3Api
@Composable
fun<T> TextFieldWithHint(
    modifier: Modifier = Modifier,
    hintList: List<T>,
    hasTitle: Boolean,
    titleText: String,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    onHintUsed: () -> Unit,
    onValueChanged: (String) -> Unit
) {

    val textValue = remember {
        mutableStateOf("")
    }

    val hintShowing = remember {
        mutableStateOf(false)
    }
    Column(verticalArrangement = Arrangement.Bottom) {
        if (hintShowing.value) {
                ExpandableHintContent(hintList = hintList) {
                    textValue.value = it
                    onValueChanged(it)
                    onHintUsed()
                    hintShowing.value = false
                }
            }
        Row(
            modifier = Modifier.align(Alignment.End),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            if (hasTitle) {
                Text(
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .weight(0.4F),
                    text = titleText,
                    style = MaterialTheme.typography.body1,
                    textAlign = TextAlign.Center
                )
            }

            TextField(
                value = textValue.value,
                onValueChange = {
                    textValue.value = it
                    onValueChanged(it)
                    hintShowing.value = false
                },
                keyboardOptions = keyboardOptions,
                keyboardActions = keyboardActions,
                trailingIcon = {
                    IconButton(modifier = Modifier, onClick = {
                        hintShowing.value = !hintShowing.value
                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_arrow_drop_up),
                            contentDescription = null
                        )
                    }
                }
            )
        }

    }

}


@Composable
fun <T> ExpandableHintContent(hintList: List<T>, onItemClick: (String) -> Unit) {
    val scrollState = rememberLazyListState()
    val stringsList: MutableList<String> = mutableListOf()

    if (hintList.isNotEmpty()) {
        when (hintList[0]) {
            is CategoriesDb -> {
                val hints: List<CategoriesDb> = hintList as List<CategoriesDb>
                for (i in hintList.indices) {
                    stringsList.add(hints[i].title)
                }
            }

            else -> {}
    }


    }
    LazyColumn(state = scrollState) {
        itemsIndexed(stringsList) { index, item ->
            Card(modifier = Modifier
                .wrapContentWidth()
                .padding(4.dp),
            elevation = 0.3.dp) {
                Row(modifier = Modifier.clickable {
                    onItemClick(item)
                }, verticalAlignment = Alignment.CenterVertically) {
                    Spacer(modifier = Modifier.weight(1F))
                    androidx.compose.material3.Text(
                        modifier = Modifier.padding(vertical = 4.dp),
                        text = item,
                        style = androidx.compose.material3.MaterialTheme.typography.bodyLarge
                    )
                }
            }

        }
    }
}


