package com.zgrcan.vefamobil.presentation.screen

import androidx.compose.runtime.Composable

@Composable
fun PersonnelLoginScreen(
    onBackClick: () -> Unit,
    onLoginClick: (username: String, password: String) -> Unit,
) {
    RoleLoginScreen(
        title = "Personel Girişi",
        usernameLabel = "Personel kullanıcı adı",
        onBackClick = onBackClick,
        onLoginClick = onLoginClick,
    )
}
