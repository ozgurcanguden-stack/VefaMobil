package com.zgrcan.vefamobil.presentation.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    isReady: Boolean = true,
    onFinished: () -> Unit,
) {
    LaunchedEffect(isReady) {
        if (!isReady) return@LaunchedEffect
        delay(600)
        onFinished()
    }

    VefaScreenLayout(
        title = "Vefa Mobil",
        subtitle = "Ulusal Vefa Programı",
    ) {}
}
