package com.zgrcan.vefamobil.data.firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class FirebaseAuthManager {
    suspend fun login(email: String, password: String): Result<Unit> {
        val auth = authOrNull() ?: return Result.failure(
            IllegalStateException("Firebase Authentication is not configured."),
        )

        return suspendCancellableCoroutine { continuation ->
            try {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (!continuation.isActive) return@addOnCompleteListener

                        if (task.isSuccessful) {
                            continuation.resume(Result.success(Unit))
                        } else {
                            val exception = task.exception ?: IllegalStateException("Firebase login failed.")
                            continuation.resume(Result.failure(exception))
                        }
                    }
            } catch (exception: Exception) {
                if (continuation.isActive) {
                    continuation.resume(Result.failure(exception))
                }
            }
        }
    }

    fun logout() {
        try {
            authOrNull()?.signOut()
        } catch (_: Exception) {
        }
    }

    fun currentUser(): FirebaseUser? {
        return try {
            authOrNull()?.currentUser
        } catch (_: Exception) {
            null
        }
    }

    suspend fun updatePassword(newPassword: String): Result<Unit> {
        return try {
            val user = currentUser() ?: return Result.failure(
                IllegalStateException("Firebase user is not authenticated."),
            )
            user.updatePassword(newPassword).await()
            Result.success(Unit)
        } catch (exception: Exception) {
            Result.failure(exception)
        }
    }

    fun isLoggedIn(): Boolean = currentUser() != null

    private fun authOrNull(): FirebaseAuth? {
        return try {
            FirebaseAuth.getInstance()
        } catch (_: Exception) {
            null
        }
    }
}
