package com.zgrcan.vefamobil.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.zgrcan.vefamobil.presentation.ManagerLoginUiState

@Composable
fun ManagerLoginScreen(
    state: ManagerLoginUiState,
    onBackClick: () -> Unit,
    onLoginClick: (organizationCode: String, email: String, password: String) -> Unit,
    onErrorShown: () -> Unit,
) {
    var organizationCode by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(state.errorMessage) {
        val message = state.errorMessage ?: return@LaunchedEffect
        snackbarHostState.showSnackbar(message)
        onErrorShown()
    }

    VefaScreenLayout(
        title = "Müdür Girişi",
        subtitle = "Firebase Authentication ile giriş.",
    ) {
        SnackbarHost(hostState = snackbarHostState)

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = organizationCode,
            onValueChange = { organizationCode = it },
            label = { Text(text = "Kurum Kodu") },
            enabled = !state.isLoading,
            singleLine = true,
        )

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = email,
            onValueChange = { email = it },
            label = { Text(text = "Kullanıcı Adı / E-posta") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            enabled = !state.isLoading,
            singleLine = true,
        )

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = password,
            onValueChange = { password = it },
            label = { Text(text = "Şifre") },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            enabled = !state.isLoading,
            singleLine = true,
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            OutlinedButton(
                modifier = Modifier.weight(1f),
                onClick = onBackClick,
                enabled = !state.isLoading,
            ) {
                Text(text = "Geri")
            }

            Button(
                modifier = Modifier.weight(1f),
                onClick = { onLoginClick(organizationCode, email, password) },
                enabled = !state.isLoading,
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator()
                } else {
                    Text(text = "Giriş Yap")
                }
            }
        }
    }
}
