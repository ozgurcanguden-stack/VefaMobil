package com.zgrcan.vefamobil.presentation.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import kotlinx.coroutines.delay

@Composable
fun ForcePasswordChangeScreen(
    onPasswordChanged: () -> Unit,
) {
    var newPassword by rememberSaveable { mutableStateOf("") }
    var confirmPassword by rememberSaveable { mutableStateOf("") }
    var errorMessage by rememberSaveable { mutableStateOf<String?>(null) }
    var successMessage by rememberSaveable { mutableStateOf<String?>(null) }

    LaunchedEffect(errorMessage) {
        if (!errorMessage.isNullOrBlank()) {
            delay(2600)
            errorMessage = null
        }
    }

    LaunchedEffect(successMessage) {
        if (!successMessage.isNullOrBlank()) {
            delay(2200)
            successMessage = null
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
                value = newPassword,
                onValueChange = { newPassword = it },
                label = { Text(text = "Yeni şifre") },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                singleLine = true,
            )

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text(text = "Yeni şifre tekrar") },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                singleLine = true,
            )

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    if (newPassword != confirmPassword) {
                        errorMessage = "Şifreler eşleşmiyor."
                    } else {
                        successMessage = "Şifreniz başarıyla değiştirilmiştir."
                    }
                },
            ) {
                Text(text = "Kaydet")
            }
        }

        TopNotification(
            message = errorMessage,
            type = TopNotificationType.ERROR,
            modifier = Modifier.align(Alignment.TopCenter),
        )

        TopNotification(
            message = successMessage,
            type = TopNotificationType.SUCCESS,
            modifier = Modifier.align(Alignment.TopCenter),
        )
    }
}
