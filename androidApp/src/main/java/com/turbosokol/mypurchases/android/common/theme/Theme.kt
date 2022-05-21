package com.turbosokol.mypurchases.android.common.theme

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

object AppTheme {
    val appButtonElevation: Dp = 4.dp
    val appLazyColumnItemElevation: Dp = 8.dp
    val appLazyColumnItemEditingElevation: Dp = 24.dp
    val appBorderStroke: BorderStroke = BorderStroke(width = 0.4.dp, color = Color.LightGray)
    val appPaddingMedium: Dp = 8.dp
}


private val DarkColorPalette = darkColors(
    primary = MyPrimary,
    primaryVariant = Purple700,
    secondary = Teal200
)


private val LightColorPalette = lightColors(
    primary = MyPrimary,
    primaryVariant = Purple700,
    secondary = Teal200

    /* Other default colors to override
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    */
)

@Composable
fun MyPurchasesTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }



    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}