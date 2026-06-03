package com.vefamobil.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

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
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = VefaTypography,
        content = content,
    )
}
