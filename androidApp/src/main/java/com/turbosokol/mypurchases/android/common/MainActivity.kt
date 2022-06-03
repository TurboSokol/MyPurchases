package com.turbosokol.mypurchases.android.common

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.ExperimentalTransitionApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.ExperimentalComposeUiApi
import com.google.accompanist.insets.ProvideWindowInsets
import com.turbosokol.mypurchases.android.common.theme.MyPurchasesTheme
import kotlin.time.ExperimentalTime
@ExperimentalTransitionApi
@ExperimentalMaterial3Api
@ExperimentalComposeUiApi
@ExperimentalMaterialApi
@ExperimentalAnimationApi
@ExperimentalTime
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyPurchasesTheme {
                ProvideWindowInsets {
                    AppNavigation()
                }
            }
        }
    }
}