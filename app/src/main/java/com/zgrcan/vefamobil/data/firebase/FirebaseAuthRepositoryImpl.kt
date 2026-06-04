package com.zgrcan.vefamobil.data.firebase

import com.google.firebase.auth.FirebaseUser
import com.zgrcan.vefamobil.repository.FirebaseAuthRepository

class FirebaseAuthRepositoryImpl(
    private val authManager: FirebaseAuthManager = FirebaseAuthManager(),
) : FirebaseAuthRepository {
    override suspend fun login(email: String, password: String): Result<Unit> {
        return authManager.login(email = email, password = password)
    }

    override fun logout() {
        authManager.logout()
    }

    override fun currentUser(): FirebaseUser? {
        return authManager.currentUser()
    }

    override suspend fun updatePassword(newPassword: String): Result<Unit> {
        return authManager.updatePassword(newPassword)
    }

    override fun isLoggedIn(): Boolean {
        return authManager.isLoggedIn()
    }
}
