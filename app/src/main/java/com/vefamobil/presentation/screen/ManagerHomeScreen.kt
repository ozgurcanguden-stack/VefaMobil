package com.vefamobil.presentation.screen

import androidx.compose.runtime.Composable

@Composable
fun ManagerHomeScreen(
    displayName: String,
) {
    VefaScreenLayout(
        title = "Müdür Ana Sayfa",
        subtitle = "Hoş geldiniz, ${displayName.ifBlank { "Vefa Müdürü" }}",
    ) {}
}
