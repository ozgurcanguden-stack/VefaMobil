package com.zgrcan.vefamobil.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.zgrcan.vefamobil.presentation.ManagerLoginTarget
import com.zgrcan.vefamobil.presentation.ManagerLoginUiState
import kotlinx.coroutines.delay

@Composable
fun ManagerLoginScreen(
    state: ManagerLoginUiState,
    onBackClick: () -> Unit,
    onOrganizationCodeChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onRememberMeChange: (Boolean) -> Unit,
    onChangeOrganizationClick: () -> Unit,
    onChangeLoginTypeClick: () -> Unit,
    onLoginClick: () -> Unit,
    onLoginSuccess: (ManagerLoginTarget) -> Unit,
    onErrorShown: () -> Unit,
    onSuccessShown: () -> Unit,
) {
    LaunchedEffect(state.errorMessage) {
        if (!state.errorMessage.isNullOrBlank()) {
            delay(2600)
            onErrorShown()
        }
    }

    LaunchedEffect(state.successTarget) {
        val target = state.successTarget ?: return@LaunchedEffect
        delay(450)
        onSuccessShown()
        onLoginSuccess(target)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        VefaScreenLayout(
            title = "Müdür Girişi",
            subtitle = "Firebase Authentication ile giriş.",
        ) {
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = state.organizationCode,
                onValueChange = onOrganizationCodeChange,
                label = { Text(text = "Kurum Kodu") },
                enabled = !state.isLoading && !state.isOrganizationCodeLocked,
                singleLine = true,
            )

            if (state.isOrganizationCodeLocked) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Text(
                        text = "Kurum kodu bu cihaz için sabitlenmiştir.",
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.68f),
                        style = MaterialTheme.typography.bodySmall,
                    )

                    OutlinedButton(
                        onClick = onChangeOrganizationClick,
                        enabled = !state.isLoading,
                    ) {
                        Text(text = "Kurum Değiştir")
                    }
                }
            }

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = state.email,
                onValueChange = onEmailChange,
                label = { Text(text = "Kullanıcı Adı / E-posta") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                enabled = !state.isLoading,
                singleLine = true,
            )

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = state.password,
                onValueChange = onPasswordChange,
                label = { Text(text = "Şifre") },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                enabled = !state.isLoading,
                singleLine = true,
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Checkbox(
                    checked = state.rememberMe,
                    onCheckedChange = onRememberMeChange,
                    enabled = !state.isLoading && !state.isOrganizationCodeLocked,
                )

                Text(
                    text = "Beni Hatırla",
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium,
                )
            }

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
                    onClick = onLoginClick,
                    enabled = !state.isLoading,
                ) {
                    if (state.isLoading) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(18.dp),
                                strokeWidth = 2.dp,
                            )
                            Text(text = "Giriş yapılıyor...")
                        }
                    } else {
                        Text(text = "Giriş Yap")
                    }
                }
            }

            TextButton(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                onClick = onChangeLoginTypeClick,
                enabled = !state.isLoading,
            ) {
                Text(text = "Giriş türünü değiştir")
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
