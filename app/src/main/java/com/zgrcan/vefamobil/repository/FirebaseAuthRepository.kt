package com.zgrcan.vefamobil.repository

import com.google.firebase.auth.FirebaseUser

interface FirebaseAuthRepository {
    suspend fun login(email: String, password: String): Result<Unit>

    fun logout()

    fun currentUser(): FirebaseUser?

    fun isLoggedIn(): Boolean
}
