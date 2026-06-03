package com.vefamobil.presentation.screen

import androidx.compose.runtime.Composable

@Composable
fun PersonnelHomeScreen(
    displayName: String,
) {
    VefaScreenLayout(
        title = "Personel Ana Sayfa",
        subtitle = "Hoş geldiniz, ${displayName.ifBlank { "Saha Personeli" }}",
    ) {}
}
