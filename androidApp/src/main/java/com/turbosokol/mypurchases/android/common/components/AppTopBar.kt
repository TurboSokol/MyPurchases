package com.turbosokol.mypurchases.android.common.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.turbosokol.mypurchases.android.R

@Composable
fun AppTopBar(
    title: String? = null,
    hasBackButton: Boolean = true,
    onBackClick: () -> Unit,
    hasOptionsButton: Boolean,
    onOptionsClick: () -> Unit,
    hasRightButton: Boolean = false,
    onRightClick: () -> Unit,
    rightContentType: RightTopBarContentType? = null,
    topBarHideState: TopBarHideState = TopBarHideState.SHOWN
) {


    TopAppBar() {
        Row(modifier = Modifier.weight(1F).padding(horizontal = 8.dp), horizontalArrangement = Arrangement.SpaceBetween) {
            if (hasBackButton) {
                AppTopBarBackButton() {
                    onBackClick()
                }
            }

            Spacer(modifier = Modifier.weight(1f))
            Text(text = title ?: "", style = MaterialTheme.typography.h6)
            Spacer(modifier = Modifier.weight(1f))

            if (hasOptionsButton) {
                AppTopBarOptionsButton() {
                    onOptionsClick()
                }
            }
            if (hasRightButton) {
                AppTopBarRightButton(rightContentType = rightContentType) {
                    onRightClick()
                }
            }
        }
    }

}

@Composable
fun AppTopBarOptionsButton(modifier: Modifier = Modifier.padding(end = 8.dp), onOptionsClick: () -> Unit) {
    Icon(modifier = modifier.clickable {
        onOptionsClick()
    }, painter = painterResource(id = R.drawable.ic_list), contentDescription = null)
}

@Composable
fun AppTopBarBackButton(backButtonOnClick: () -> Unit) {
    Icon(
        modifier = Modifier.clickable {
            backButtonOnClick()
        }, painter = painterResource(id = R.drawable.ic_arrow_back),
        contentDescription = null
    )
}

@Composable
fun AppTopBarRightButton(
    rightContentType: RightTopBarContentType?,
    rightButtonOnClick: () -> Unit
) {
    val rightButtonIcon: Int = when (rightContentType) {
        RightTopBarContentType.DELETE -> {
            R.drawable.ic_delete
        }
        RightTopBarContentType.ADD -> {
            R.drawable.ic_add_circle
        }
        RightTopBarContentType.APP_INFO -> {
            R.drawable.ic_info
        }
        else -> {
            R.drawable.ic_info
        }
    }
    Icon(
        modifier = Modifier.clickable {
            rightButtonOnClick()
        },
        painter = painterResource(id = rightButtonIcon),
        contentDescription = null
    )
}

enum class RightTopBarContentType {
    DELETE, ADD, APP_INFO
}

enum class TopBarHideState {
    SHOWN,
    HIDDEN
}