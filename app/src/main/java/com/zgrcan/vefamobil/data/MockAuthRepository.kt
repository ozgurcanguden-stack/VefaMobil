package com.zgrcan.vefamobil.data

import com.zgrcan.vefamobil.model.MockUser
import com.zgrcan.vefamobil.model.UserRole
import com.zgrcan.vefamobil.repository.AuthRepository

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
