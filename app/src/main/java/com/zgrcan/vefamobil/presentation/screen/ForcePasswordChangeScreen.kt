package com.zgrcan.vefamobil.presentation.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import com.zgrcan.vefamobil.presentation.ForcePasswordChangeUiState
import kotlinx.coroutines.delay

@Composable
fun ForcePasswordChangeScreen(
    state: ForcePasswordChangeUiState,
    useFirebasePasswordChange: Boolean,
    onNewPasswordChange: (String) -> Unit,
    onConfirmPasswordChange: (String) -> Unit,
    onChangePasswordClick: () -> Unit,
    onMockPasswordChangeClick: () -> Unit,
    onErrorShown: () -> Unit,
    onSuccessShown: () -> Unit,
    onPasswordChanged: () -> Unit,
) {
    LaunchedEffect(state.errorMessage) {
        if (!state.errorMessage.isNullOrBlank()) {
            delay(2600)
            onErrorShown()
        }
    }

    LaunchedEffect(state.successMessage) {
        if (!state.successMessage.isNullOrBlank()) {
            delay(2200)
            onSuccessShown()
            onPasswordChanged()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        VefaScreenLayout(
            title = "Şifre Değiştir",
            subtitle = "Devam etmek için yeni şifrenizi belirleyin.",
        ) {
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = state.newPassword,
                onValueChange = onNewPasswordChange,
                label = { Text(text = "Yeni şifre") },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                enabled = !state.isLoading,
                singleLine = true,
            )

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = state.confirmPassword,
                onValueChange = onConfirmPasswordChange,
                label = { Text(text = "Yeni şifre tekrar") },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                enabled = !state.isLoading,
                singleLine = true,
            )

            Button(
                modifier = Modifier.fillMaxWidth(),
                enabled = !state.isLoading,
                onClick = {
                    if (useFirebasePasswordChange) {
                        onChangePasswordClick()
                    } else {
                        onMockPasswordChangeClick()
                    }
                },
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator()
                } else {
                    Text(text = "Şifreyi Değiştir")
                }
            }
        }

        TopNotification(
            message = state.errorMessage,
            type = TopNotificationType.ERROR,
            modifier = Modifier.align(Alignment.TopCenter),
        )

        TopNotification(
            message = state.successMessage,
            type = TopNotificationType.SUCCESS,
            modifier = Modifier.align(Alignment.TopCenter),
        )
    }
}
