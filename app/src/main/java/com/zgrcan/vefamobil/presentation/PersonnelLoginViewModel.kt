package com.zgrcan.vefamobil.presentation

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.zgrcan.vefamobil.data.firebase.FirebaseAuthRepositoryImpl
import com.zgrcan.vefamobil.data.firebase.FirestoreUserRepository
import com.zgrcan.vefamobil.data.preferences.LoginPreferencesManager
import com.zgrcan.vefamobil.model.AppUser
import com.zgrcan.vefamobil.model.Organization
import com.zgrcan.vefamobil.repository.FirebaseAuthRepository
import java.util.Locale
import kotlinx.coroutines.launch

data class PersonnelLoginUiState(
    val organizationCode: String = "",
    val email: String = "",
    val password: String = "",
    val rememberMe: Boolean = false,
    val isOrganizationCodeLocked: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null,
    val successTarget: PersonnelLoginTarget? = null,
)

enum class PersonnelLoginTarget {
    PERSONNEL_HOME,
}

class PersonnelLoginViewModel(
    application: Application,
) : AndroidViewModel(application) {
    private val authRepository: FirebaseAuthRepository = FirebaseAuthRepositoryImpl()
    private val userRepository = FirestoreUserRepository()
    private val loginPreferencesManager = LoginPreferencesManager(application.applicationContext)

    var state by mutableStateOf(PersonnelLoginUiState())
        private set

    var currentUser: AppUser? = null
        private set

    var currentOrganization: Organization? = null
        private set

    init {
        viewModelScope.launch {
            loginPreferencesManager.personnelLoginPreferences.collect { preferences ->
                state = state.copy(
                    organizationCode = if (preferences.rememberMe) preferences.organizationCode else state.organizationCode,
                    email = if (preferences.rememberMe) preferences.email else state.email,
                    rememberMe = preferences.rememberMe,
                    isOrganizationCodeLocked = preferences.rememberMe && preferences.organizationCode.isNotBlank(),
                )
            }
        }
    }

    fun onOrganizationCodeChange(value: String) {
        if (!state.isOrganizationCodeLocked) {
            state = state.copy(organizationCode = value)
        }
    }

    fun onEmailChange(value: String) {
        state = state.copy(email = value)
    }

    fun onPasswordChange(value: String) {
        state = state.copy(password = value)
    }

    fun onRememberMeChange(rememberMe: Boolean) {
        if (!state.isOrganizationCodeLocked) {
            state = state.copy(rememberMe = rememberMe)
        }
    }

    fun clearSavedPersonnelLogin() {
        viewModelScope.launch {
            loginPreferencesManager.clearPersonnelLogin()
        }
        state = state.copy(
            organizationCode = "",
            email = "",
            password = "",
            rememberMe = false,
            isOrganizationCodeLocked = false,
        )
    }

    fun login() {
        val organizationCode = state.organizationCode.trim()
        val email = state.email.trim()
        val password = state.password
        val rememberMe = state.rememberMe

        if (email.isBlank() || password.isBlank()) {
            showError("Giriş başarısız. Bilgilerinizi kontrol ediniz.")
            return
        }

        viewModelScope.launch {
            state = state.copy(
                isLoading = true,
                errorMessage = null,
                successMessage = null,
                successTarget = null,
            )
            currentUser = null
            currentOrganization = null

            val authResult = authRepository.login(email = email, password = password)
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

            if (!user.role.equals("PERSONNEL", ignoreCase = true)) {
                failAfterAuthenticated("Bu kullanıcı personel yetkisine sahip değil.")
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

            if (rememberMe) {
                loginPreferencesManager.savePersonnelLogin(
                    organizationCode = organizationCode,
                    email = email,
                )
            } else {
                loginPreferencesManager.clearPersonnelLogin()
            }

            currentUser = user
            currentOrganization = organization
            state = state.copy(
                password = "",
                rememberMe = rememberMe,
                isOrganizationCodeLocked = rememberMe && organizationCode.isNotBlank(),
                isLoading = true,
                errorMessage = null,
                successMessage = "Giriş başarılı.",
                successTarget = PersonnelLoginTarget.PERSONNEL_HOME,
            )
        }
    }

    suspend fun restoreSession(): PersonnelLoginTarget? {
        if (!authRepository.isLoggedIn()) return null

        val uid = authRepository.currentUser()?.uid
        if (uid.isNullOrBlank()) return null

        val user = userRepository.getUserProfile(uid) ?: run {
            authRepository.logout()
            return null
        }

        if (!user.role.equals("PERSONNEL", ignoreCase = true)) return null

        if (!user.isActive) {
            authRepository.logout()
            return null
        }

        val organization = userRepository.getOrganization(user.organizationId)
        if (organization == null || !organization.isActive) {
            authRepository.logout()
            return null
        }

        currentUser = user
        currentOrganization = organization
        return PersonnelLoginTarget.PERSONNEL_HOME
    }

    fun logout() {
        authRepository.logout()
        currentUser = null
        currentOrganization = null
        state = state.copy(
            password = "",
            isLoading = false,
            errorMessage = null,
            successMessage = null,
            successTarget = null,
        )
    }

    fun clearError() {
        state = state.copy(errorMessage = null)
    }

    fun clearSuccess() {
        state = state.copy(
            isLoading = false,
            successMessage = null,
            successTarget = null,
        )
    }

    private fun failAfterAuthenticated(message: String) {
        authRepository.logout()
        currentUser = null
        currentOrganization = null
        showError(message)
    }

    private fun showError(message: String) {
        state = state.copy(
            isLoading = false,
            errorMessage = message,
            successMessage = null,
            successTarget = null,
        )
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
