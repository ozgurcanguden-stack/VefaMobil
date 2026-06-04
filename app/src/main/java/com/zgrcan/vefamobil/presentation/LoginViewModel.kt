package com.zgrcan.vefamobil.presentation

import androidx.lifecycle.ViewModel
import com.zgrcan.vefamobil.data.MockAuthRepository
import com.zgrcan.vefamobil.domain.LoginUseCase
import com.zgrcan.vefamobil.model.MockUser
import com.zgrcan.vefamobil.model.UserRole

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
