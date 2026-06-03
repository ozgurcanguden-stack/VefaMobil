package com.vefamobil.presentation.screen

import androidx.compose.runtime.Composable

@Composable
fun ManagerLoginScreen(
    onBackClick: () -> Unit,
    onLoginClick: (username: String, password: String) -> Unit,
) {
    RoleLoginScreen(
        title = "Müdür Girişi",
        usernameLabel = "Müdür kullanıcı adı",
        onBackClick = onBackClick,
        onLoginClick = onLoginClick,
    )
}
