package com.vefamobil.domain

import com.vefamobil.model.MockUser
import com.vefamobil.model.UserRole
import com.vefamobil.repository.AuthRepository

class LoginUseCase(
    private val authRepository: AuthRepository,
) {
    operator fun invoke(role: UserRole, username: String, password: String): MockUser {
        return authRepository.login(role, username, password)
    }
}
