package com.vefamobil.data

import com.vefamobil.model.MockUser
import com.vefamobil.model.UserRole
import com.vefamobil.repository.AuthRepository

class MockAuthRepository : AuthRepository {
    override fun login(role: UserRole, username: String, password: String): MockUser {
        val fallbackName = when (role) {
            UserRole.MANAGER -> "Vefa Müdürü"
            UserRole.PERSONNEL -> "Saha Personeli"
        }

        return MockUser(
            id = role.name.lowercase(),
            displayName = username.ifBlank { fallbackName },
            role = role,
            mustChangePassword = password == "change",
        )
    }
}
