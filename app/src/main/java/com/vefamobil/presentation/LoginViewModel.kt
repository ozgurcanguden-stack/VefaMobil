package com.vefamobil.presentation

import androidx.lifecycle.ViewModel
import com.vefamobil.data.MockAuthRepository
import com.vefamobil.domain.LoginUseCase
import com.vefamobil.model.MockUser
import com.vefamobil.model.UserRole

class LoginViewModel : ViewModel() {
    private val loginUseCase = LoginUseCase(MockAuthRepository())

    var currentUser: MockUser? = null
        private set

    fun login(role: UserRole, username: String, password: String): MockUser {
        return loginUseCase(role, username.trim(), password).also { user ->
            currentUser = user
        }
    }
}
