package com.turbosokol.mypurchases.android.common

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.ExperimentalMaterialApi
import com.google.accompanist.insets.ProvideWindowInsets
import com.turbosokol.mypurchases.android.common.theme.MyPurchasesTheme
import kotlin.time.ExperimentalTime

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