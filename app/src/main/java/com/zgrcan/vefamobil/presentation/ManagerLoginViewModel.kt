package com.zgrcan.vefamobil.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zgrcan.vefamobil.data.firebase.FirebaseAuthRepositoryImpl
import com.zgrcan.vefamobil.data.firebase.FirestoreUserRepository
import com.zgrcan.vefamobil.model.AppUser
import com.zgrcan.vefamobil.model.Organization
import com.zgrcan.vefamobil.repository.FirebaseAuthRepository
import java.util.Locale
import kotlinx.coroutines.launch

data class ManagerLoginUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
)

enum class ManagerLoginTarget {
    FORCE_PASSWORD_CHANGE,
    MANAGER_HOME,
}

class ManagerLoginViewModel(
    private val authRepository: FirebaseAuthRepository = FirebaseAuthRepositoryImpl(),
    private val userRepository: FirestoreUserRepository = FirestoreUserRepository(),
) : ViewModel() {
    var state by mutableStateOf(ManagerLoginUiState())
        private set

    var currentUser: AppUser? = null
        private set

    var currentOrganization: Organization? = null
        private set

    fun login(
        organizationCode: String,
        email: String,
        password: String,
        onSuccess: (ManagerLoginTarget) -> Unit,
    ) {
        val trimmedEmail = email.trim()
        if (trimmedEmail.isBlank() || password.isBlank()) {
            showError("Giriş başarısız. Bilgilerinizi kontrol ediniz.")
            return
        }

        viewModelScope.launch {
            state = state.copy(isLoading = true, errorMessage = null)
            currentUser = null
            currentOrganization = null

            val authResult = authRepository.login(email = trimmedEmail, password = password)
            if (authResult.isFailure) {
                showError("Giriş başarısız. Bilgilerinizi kontrol ediniz.")
                return@launch
            }

            val uid = authRepository.currentUser()?.uid
            if (uid.isNullOrBlank()) {
                failAfterAuthenticated("Kullanıcı profili bulunamadı.")
                return@launch
            }

            val user = userRepository.getUserProfile(uid)
            if (user == null) {
                failAfterAuthenticated("Kullanıcı profili bulunamadı.")
                return@launch
            }

            if (!user.role.equals("MANAGER", ignoreCase = true)) {
                failAfterAuthenticated("Bu kullanıcı müdür yetkisine sahip değil.")
                return@launch
            }

            if (!user.isActive) {
                failAfterAuthenticated("Kullanıcı pasif durumda.")
                return@launch
            }

            val organization = userRepository.getOrganization(user.organizationId)
            if (organization == null) {
                failAfterAuthenticated("Kurum kaydı bulunamadı.")
                return@launch
            }

            if (!organization.isActive) {
                failAfterAuthenticated("Kurum pasif durumda.")
                return@launch
            }

            if (normalizeOrganizationCode(organizationCode) != normalizeOrganizationCode(organization.organizationCode)) {
                failAfterAuthenticated("Kurum kodu hatalı.")
                return@launch
            }

            currentUser = user
            currentOrganization = organization
            state = state.copy(isLoading = false, errorMessage = null)

            val target = if (user.mustChangePassword) {
                ManagerLoginTarget.FORCE_PASSWORD_CHANGE
            } else {
                ManagerLoginTarget.MANAGER_HOME
            }
            onSuccess(target)
        }
    }

    fun logout() {
        authRepository.logout()
        currentUser = null
        currentOrganization = null
    }

    fun clearError() {
        state = state.copy(errorMessage = null)
    }

    private fun failAfterAuthenticated(message: String) {
        authRepository.logout()
        currentUser = null
        currentOrganization = null
        showError(message)
    }

    private fun showError(message: String) {
        state = state.copy(isLoading = false, errorMessage = message)
    }

    private fun normalizeOrganizationCode(value: String): String {
        var normalized = value.trim().uppercase(Locale("tr", "TR"))
        val replacements = mapOf(
            "İ" to "I",
            "İ" to "I",
            "Ç" to "C",
            "Ğ" to "G",
            "Ö" to "O",
            "Ş" to "S",
            "Ü" to "U",
            "Â" to "A",
            "Î" to "I",
            "Û" to "U",
        )

        replacements.forEach { (source, target) ->
            normalized = normalized.replace(source, target)
        }

        return normalized.filter { it.isLetterOrDigit() }
    }
}
