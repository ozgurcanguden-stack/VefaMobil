package com.zgrcan.vefamobil.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuthRecentLoginRequiredException
import com.zgrcan.vefamobil.data.firebase.FirebaseAuthRepositoryImpl
import com.zgrcan.vefamobil.data.firebase.FirestoreUserRepository
import com.zgrcan.vefamobil.repository.FirebaseAuthRepository
import kotlinx.coroutines.launch

data class ForcePasswordChangeUiState(
    val newPassword: String = "",
    val confirmPassword: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null,
)

class ForcePasswordChangeViewModel(
    private val authRepository: FirebaseAuthRepository = FirebaseAuthRepositoryImpl(),
    private val userRepository: FirestoreUserRepository = FirestoreUserRepository(),
) : ViewModel() {
    var state by mutableStateOf(ForcePasswordChangeUiState())
        private set

    fun onNewPasswordChange(value: String) {
        state = state.copy(newPassword = value)
    }

    fun onConfirmPasswordChange(value: String) {
        state = state.copy(confirmPassword = value)
    }

    fun changePassword() {
        val newPassword = state.newPassword
        val confirmPassword = state.confirmPassword

        if (newPassword != confirmPassword) {
            showError("Şifreler eşleşmiyor.")
            return
        }

        if (newPassword.length < 6) {
            showError("Şifre en az 6 karakter olmalıdır.")
            return
        }

        viewModelScope.launch {
            state = state.copy(isLoading = true, errorMessage = null, successMessage = null)

            val uid = authRepository.currentUser()?.uid
            if (uid.isNullOrBlank()) {
                showError("Oturum süresi doldu. Lütfen tekrar giriş yapınız.")
                return@launch
            }

            val passwordResult = authRepository.updatePassword(newPassword)
            if (passwordResult.isFailure) {
                showError(passwordResult.exceptionOrNull().toPasswordChangeMessage())
                return@launch
            }

            val profileResult = userRepository.markPasswordChangeCompleted(uid)
            if (profileResult.isFailure) {
                showError("Şifre değiştirilemedi. Lütfen tekrar deneyiniz.")
                return@launch
            }

            state = state.copy(
                newPassword = "",
                confirmPassword = "",
                isLoading = false,
                errorMessage = null,
                successMessage = "Şifreniz başarıyla değiştirilmiştir.",
            )
        }
    }

    fun completeMockPasswordChange() {
        if (state.newPassword != state.confirmPassword) {
            showError("Şifreler eşleşmiyor.")
            return
        }

        if (state.newPassword.length < 6) {
            showError("Şifre en az 6 karakter olmalıdır.")
            return
        }

        state = state.copy(
            newPassword = "",
            confirmPassword = "",
            isLoading = false,
            errorMessage = null,
            successMessage = "Şifreniz başarıyla değiştirilmiştir.",
        )
    }

    fun clearError() {
        state = state.copy(errorMessage = null)
    }

    fun clearSuccess() {
        state = state.copy(successMessage = null)
    }

    private fun showError(message: String) {
        state = state.copy(isLoading = false, errorMessage = message, successMessage = null)
    }

    private fun Throwable?.toPasswordChangeMessage(): String {
        return when (this) {
            is FirebaseAuthRecentLoginRequiredException -> "Oturum süresi doldu. Lütfen tekrar giriş yapınız."
            else -> "Şifre değiştirilemedi. Lütfen tekrar deneyiniz."
        }
    }
}
