package com.turbosokol.mypurchases.android.common

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.turbosokol.mypurchases.android.common.pages.ListsPage
import com.turbosokol.mypurchases.android.common.theme.MyPurchasesTheme
import com.turbosokol.mypurchases.android.core.ReduxViewModel
import org.koin.androidx.compose.getViewModel
import org.koin.androidx.compose.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
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