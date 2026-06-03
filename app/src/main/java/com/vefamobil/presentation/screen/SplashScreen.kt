package com.vefamobil.presentation.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onFinished: () -> Unit,
) {
    LaunchedEffect(Unit) {
        delay(600)
        onFinished()
    }

    VefaScreenLayout(
        title = "Vefa Mobil",
        subtitle = "Ulusal Vefa Programı",
    ) {}
}
