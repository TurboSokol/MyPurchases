package com.turbosokol.mypurchases.android.common.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.turbosokol.mypurchases.android.R
import com.turbosokol.mypurchases.android.common.theme.MyRedColor
import com.turbosokol.mypurchases.android.core.ReduxViewModel
import com.turbosokol.mypurchases.common.app.AppState
import com.turbosokol.mypurchases.common.navigation.redux.AppTopBarStateType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import org.koin.androidx.compose.getViewModel
import kotlin.time.ExperimentalTime

@ExperimentalTime
@Composable
fun AppTopBar(
    viewModel: ReduxViewModel = getViewModel(),
    title: String? = null,
    hasBackButton: Boolean = true,
    onBackClick: () -> Unit,
    hasOptionsButton: Boolean,
    onOptionsClick: () -> Unit,
    hasSubRightButton: Boolean,
    subRightContentType: TopBarButtonsType? = TopBarButtonsType.EDIT,
    onSubRightClick: () -> Unit,
    hasRightButton: Boolean,
    onRightClick: () -> Unit,
    rightContentType: TopBarButtonsType? = TopBarButtonsType.DELETE,
) {
    val stateFlow: StateFlow<AppState> = viewModel.store.observeAsState()
    val state by stateFlow.collectAsState(Dispatchers.Main)
    val navigationState = state.getNavigationState()
    val appTopBarStateType = navigationState.appTopBarStateType

    val animationTransition = rememberInfiniteTransition()
    val animationBorderWidth by animationTransition.animateValue(
        initialValue = 0.dp,
        targetValue = 2.dp,
        typeConverter = Dp.VectorConverter,
        animationSpec = InfiniteRepeatableSpec(
            animation = tween(500),
            repeatMode = RepeatMode.Reverse
        )
    )
    val animationAlpha by animationTransition.animateFloat(
        initialValue = 0.6F,
        targetValue = 1.0F,
        animationSpec = InfiniteRepeatableSpec(
            animation = snap(500)
        )
    )

    TopAppBar() {
        Row(
            modifier = Modifier
                .weight(1F)
                .padding(horizontal = 8.dp), horizontalArrangement = Arrangement.SpaceBetween
        ) {
            if (hasBackButton) {
                AppTopBarBackButton() {
                    onBackClick()
                }
            }

            Spacer(modifier = Modifier.weight(1f))
            Text(text = title ?: "", style = MaterialTheme.typography.h6)
            Spacer(modifier = Modifier.weight(1f))

            if (hasOptionsButton) {
                AppTopBarOptionsButton(modifier = Modifier) {
                    onOptionsClick()
                }
            }

            if (hasSubRightButton) {
                AppTopBarSubRightButton(
                    modifier = Modifier
                        .padding(end = 4.dp)
                        .border(
                            BorderStroke(
                                if (appTopBarStateType.toString() == subRightContentType.toString()) animationBorderWidth else (-1).dp,
                                MyRedColor
                            )
                        ),
                    tint = if (appTopBarStateType.toString() == subRightContentType.toString()) {
                        MyRedColor
                    } else {
                        LocalContentColor.current.copy(alpha = LocalContentAlpha.current)
                    },
                    subRightContentType = subRightContentType
                ) {
                    onSubRightClick()
                }
            }

            if (hasRightButton) {
                AppTopBarRightButton(
                    modifier = Modifier.border(
                        BorderStroke(
                            if (appTopBarStateType.toString() == rightContentType.toString()) animationBorderWidth else (-1).dp,
                            MyRedColor
                        )
                    ),
                    tint = if (appTopBarStateType.toString() == rightContentType.toString()) {
                        MyRedColor
                    } else {
                        LocalContentColor.current.copy(alpha = LocalContentAlpha.current)
                    },
                    rightContentType = rightContentType
                ) {
                    onRightClick()
                }
            }
        }
    }

}

@Composable
fun AppTopBarOptionsButton(
    modifier: Modifier,
    onOptionsClick: () -> Unit
) {
    Icon(
        modifier = modifier
            .padding(end = 6.dp)
            .clickable {
                onOptionsClick()
            }, painter = painterResource(id = R.drawable.ic_list), contentDescription = null
    )
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
fun AppTopBarSubRightButton(
    modifier: Modifier,
    tint: Color,
    subRightContentType: TopBarButtonsType?,
    onSubRightButtonClick: () -> Unit
) {
    val rightButtonIcon: Int = when (subRightContentType) {
        TopBarButtonsType.DELETE -> {
            R.drawable.ic_delete
        }
        TopBarButtonsType.ADD -> {
            R.drawable.ic_add_circle
        }
        TopBarButtonsType.APP_INFO -> {
            R.drawable.ic_info
        }
        TopBarButtonsType.EDIT -> {
            R.drawable.ic_edit
        }
        else -> {
            R.drawable.ic_info
        }
    }
    Icon(
        modifier = modifier
            .padding(horizontal = 2.dp)
            .clickable {
                onSubRightButtonClick()
            },
        painter = painterResource(id = rightButtonIcon),
        contentDescription = null,
        tint = tint
    )
}

@Composable
fun AppTopBarRightButton(
    modifier: Modifier,
    tint: Color,
    rightContentType: TopBarButtonsType?,
    onRightClick: () -> Unit
) {
    val rightButtonIcon: Int = when (rightContentType) {
        TopBarButtonsType.DELETE -> {
            R.drawable.ic_delete
        }
        TopBarButtonsType.ADD -> {
            R.drawable.ic_add_circle
        }
        TopBarButtonsType.APP_INFO -> {
            R.drawable.ic_info
        }
        else -> {
            R.drawable.ic_info
        }
    }
    Icon(
        modifier = modifier.clickable {
            onRightClick()
        },
        painter = painterResource(id = rightButtonIcon),
        contentDescription = null,
        tint = tint
    )
}

enum class TopBarButtonsType {
    DELETE, ADD, APP_INFO, EDIT
}
