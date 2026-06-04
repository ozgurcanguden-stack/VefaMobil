package com.zgrcan.vefamobil.domain

import com.zgrcan.vefamobil.model.MockUser
import com.zgrcan.vefamobil.model.UserRole
import com.zgrcan.vefamobil.repository.AuthRepository

class LoginUseCase(
    private val authRepository: AuthRepository,
) {
    operator fun invoke(role: UserRole, username: String, password: String): MockUser {
        return authRepository.login(role, username, password)
    }
}
