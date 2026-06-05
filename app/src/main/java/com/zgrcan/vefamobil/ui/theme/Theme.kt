package com.zgrcan.vefamobil.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat

private val LightColorScheme = lightColorScheme(
    primary = VefaBlue,
    onPrimary = Color.White,
    secondary = VefaBlueDark,
    onSecondary = Color.White,
    background = VefaGray,
    onBackground = Color(0xFF18212B),
    surface = Color.White,
    onSurface = Color(0xFF18212B),
    primaryContainer = VefaBlueLight,
    onPrimaryContainer = VefaBlueDark,
)

@Composable
fun VefaMobilTheme(
    content: @Composable () -> Unit,
) {
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            WindowCompat.setDecorFitsSystemWindows(window, true)
            @Suppress("DEPRECATION")
            window.statusBarColor = VefaGray.toArgb()
            @Suppress("DEPRECATION")
            window.navigationBarColor = VefaGray.toArgb()
            WindowInsetsControllerCompat(window, view).apply {
                isAppearanceLightStatusBars = true
                isAppearanceLightNavigationBars = true
            }
        }
    }

    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = VefaTypography,
        content = content,
    )
}
